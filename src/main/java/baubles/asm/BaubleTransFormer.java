package baubles.asm;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class BaubleTransFormer implements IFMLLoadingPlugin {
    boolean hasChanged = false;

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
        byte[] bytes = new byte[0];
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
        try (JarFile myjar = new JarFile(me); InputStream stream = myjar.getInputStream(myjar.getEntry("baubles/api/package-info.class"));) {
            ClassNode node = new ClassNode();
            new ClassReader(stream).accept(node, 0);
            AnnotationNode annotation = node.visibleAnnotations.get(0);
            for (int i = 0; i < annotation.values.size(); i++)
                if ("apiVersion".equals(annotation.values.get(i))) annotation.values.set(i + 1, "1.0.0.0");
            node.accept(cw);
            bytes = cw.toByteArray();
        } catch (Exception e) {
            FMLLog.log(Level.ERROR, e, "为什么读取不了自己?");
        }
        //搜索全部包含API但不含描述文件的jar并修改
        for (File mod : files) {
            try (JarFile jar = new JarFile(mod)) {
                JarEntry entry = jar.getJarEntry("baubles/api");
                if (entry != null) {
                    if (jar.getJarEntry("baubles/api/package-info.class") != null) continue;
                    injectJar(jar, bytes, mod.getParentFile());
                }
            } catch (Exception e) {
                FMLLog.log(Level.ERROR, e, "操作文件%s异常", mod);
            }
        }
        if (hasChanged) throw new RuntimeException("这是一次计划性报错,mod列表未更改的情况下不会再次发生，请再启动一次");
    }

    private void injectJar(ZipFile jar, byte[] packageinfo, File directory) throws IOException {
        hasChanged = true;
        File temp = File.createTempFile(jar.getName(), null, directory);
        temp.deleteOnExit();
        Files.copy(Paths.get(jar.getName()), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        boolean planB = false;
        while (true)
            try (ZipFile zip = new ZipFile(temp); ZipOutputStream zos = new ZipOutputStream(java.nio.file.Files.newOutputStream(Paths.get(jar.getName())))) {
                Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.isDirectory()) continue;
                    zos.putNextEntry(planB ? new ZipEntry(entry.getName()) : new ZipEntry(entry));

                    try (InputStream is = zip.getInputStream(entry)) {
                        for (int l = is.read(); l > -1; l = is.read()) zos.write(l);
                    }
                    zos.closeEntry();
                    zos.flush();
                }
                addInfo(packageinfo, zos);
                zip.close();
                temp.delete();
                break;
            } catch (ZipException e) {
                if (planB)
                    throw new RuntimeException("不支持的特殊压缩方案:" + jar.getName(), e);
                planB = true;
            }
    }

    private void addInfo(byte[] info, ZipOutputStream zos) throws IOException {
        ZipEntry out = new ZipEntry("baubles/api/package-info.class");
        zos.putNextEntry(out);
        zos.write(info);
        zos.closeEntry();
        zos.flush();
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
