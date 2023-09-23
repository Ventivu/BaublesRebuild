package baubles.asm;

import com.google.common.io.Files;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class BaubleTransFormer implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    /**
     * API冲突修复程序
     */
    @Override
    public void injectData(Map<String, Object> data) {
        List<File> files = new ArrayList<>();
        File me = (File) data.get("coremodLocation");
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        //生成jar列表
        File folder = new File((File) data.get("mcLocation"), "mods");
        File[] f = folder.listFiles(file -> file.getName().endsWith(".jar"));
        if (f != null) files.addAll(Arrays.asList(f));
        folder = new File(folder, "1.7.10");
        if (folder.exists()) {
            f = folder.listFiles(file -> file.getName().endsWith(".jar"));
            if (f != null) files.addAll(Arrays.asList(f));
        }
        //生成API描述文件
        try (JarFile myjar = new JarFile(me)) {
            InputStream stream = myjar.getInputStream(myjar.getEntry("baubles/api/package-info.class"));
            ClassReader reader = new ClassReader(stream);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            AnnotationNode annotation = node.visibleAnnotations.get(0);
            for (int i = 0; i < annotation.values.size(); i++)
                if ("apiVersion".equals(annotation.values.get(i))) annotation.values.set(i + 1, "1.0.0.0");
            node.accept(cw);

        } catch (Exception e) {
            FMLLog.log(Level.ERROR, e, "为什么读取不了自己?");
        }
        //搜索全部包含API但不含描述文件的jar并修改
        for (File mod : files) {
            try (JarFile jar = new JarFile(mod)) {
                JarEntry entry = jar.getJarEntry("baubles/api");
                if (entry != null) {
                    if (jar.getJarEntry("baubles/api/package-info.class") != null) continue;

                    File temp = File.createTempFile(mod.getName(), null, mod.getParentFile());
                    Files.copy(mod, temp);
                    JarInputStream source = new JarInputStream(java.nio.file.Files.newInputStream(temp.toPath()));
                    JarOutputStream jos = new JarOutputStream(java.nio.file.Files.newOutputStream(mod.toPath()));
                    ZipEntry e;
                    while ((e = source.getNextEntry()) != null) {
                        ZipEntry zosEntry = new ZipEntry(e);
                        zosEntry.setCompressedSize(-1);
                        Field field = ZipEntry.class.getDeclaredField("crc");
                        field.setAccessible(true);
                        field.set(zosEntry, -1);
                        field = ZipEntry.class.getDeclaredField("size");
                        field.setAccessible(true);
                        field.set(zosEntry, -1);

                        zosEntry.setComment(e.getComment());
                        zosEntry.setExtra(e.getExtra());
                        jos.putNextEntry(zosEntry);
                        IOUtils.copy(source, jos);
                    }

                    JarEntry out = new JarEntry("baubles/api/package-info.class");
                    jos.putNextEntry(out);
                    jos.write(cw.toByteArray());
                    jos.closeEntry();
                    jos.close();
                    source.close();
                    temp.delete();
                }
            } catch (Exception e) {
                FMLLog.log(Level.ERROR, e, "读取文件%s异常", mod);
            }
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
