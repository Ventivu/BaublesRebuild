package baubles.asm;

import com.google.common.io.Files;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

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

    @Override
    public void injectData(Map<String, Object> data) {
        File folder = new File((File) data.get("mcLocation"), "mods");
        File me = (File) data.get("coremodLocation");
        File[] mods = folder.listFiles(File::isFile);
        try (JarFile myjar = new JarFile(me)) {
            if (mods != null && mods.length != 0) {
                for (File mod : mods) {
                    try (JarFile jar = new JarFile(mod)) {
                        JarEntry entry = jar.getJarEntry("baubles/api");
                        if (entry != null) {
                            if (jar.getJarEntry("baubles/api/package-info.class") != null) continue;
                            InputStream stream = myjar.getInputStream(myjar.getEntry("baubles/api/package-info.class"));
                            ClassReader reader = new ClassReader(stream);
                            ClassNode node = new ClassNode();
                            reader.accept(node, 0);
                            AnnotationNode annotation = node.visibleAnnotations.get(0);
                            for (int i = 0; i < annotation.values.size(); i++)
                                if ("apiVersion".equals(annotation.values.get(i)))
                                    annotation.values.set(i + 1, "1.0.0.0");
                            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                            node.accept(cw);

                            File temp = File.createTempFile(mod.getName(), null, mod.getParentFile());
                            temp.deleteOnExit();
                            Files.copy(mod, temp);
                            JarInputStream source = new JarInputStream(java.nio.file.Files.newInputStream(temp.toPath()));
                            JarOutputStream jos = new JarOutputStream(java.nio.file.Files.newOutputStream(mod.toPath()));
                            JarEntry e;
                            while ((e=source.getNextJarEntry())!=null) {
                                jos.putNextEntry(e);
                                byte[] code=new byte[1024];
                                int length;
                                while ((length=source.read(code))>0)
                                    jos.write(code,0,length);
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
                        System.out.printf("读取文件%s异常\n", mod);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("为什么读取不了自己?");
            e.printStackTrace();
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
