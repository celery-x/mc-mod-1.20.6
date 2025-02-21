package top.superxuqc.mcmod.common;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoadParticleConfigUtil {
    public static List<List<Float[]>> lagerList = new ArrayList<>();
    public static List<List<Float[]>> mediumList = new ArrayList<>();
    public static List<List<Float[]>> smallList = new ArrayList<>();

    public static void initParticle() {
        for (int i = 4; i <= 16; i++) {
            LoadParticleConfigUtil.loadParticle(lagerList, "large-fazhen-" + i);
        }
        for (int i = 2; i <= 12; i++) {
            LoadParticleConfigUtil.loadParticle(mediumList, "medium-fazhen-" + i);
        }
        for (int i = 1; i <= 4; i++) {
            LoadParticleConfigUtil.loadParticle(smallList, "small-fazhen-" + i);
        }
    }

    public static void loadParticle(List<List<Float[]>> alist, String functionName) {


        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.of("particle-resources", functionName);
            }

            @Override
            public void reload(ResourceManager manager) {
                Identifier click = Identifier.of("particle-resources", functionName + ".mcfunction");
                Optional<Resource> resource = manager.getResource(click);
                if (resource.isPresent()) {
                    try (InputStream stream = resource.get().getInputStream()) {
                        List<Float[]> list = getList(stream);
                        if (!list.isEmpty()) {
                            alist.add(list);
                        }
                        // Consume the stream however you want, medium, rare, or well done.
                    } catch (Exception e) {
                        System.out.println("Error occurred while loading resource json" + e);
                    }
                }
            }

            @NotNull
            private List<Float[]> getList(InputStream stream) throws IOException {
                List<Float[]> list = new ArrayList<>();
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("particle minecraft:")) {
                        Float[] part = new Float[6];
                        String[] s = line.split(" ");
                        part[0] = Float.parseFloat(s[2]);
                        part[1] = Float.parseFloat(s[3]);
                        part[2] = Float.parseFloat(s[4]);

                        part[3] = Float.parseFloat(s[6].replace("^", "").replace("~", ""));
                        part[4] = Float.parseFloat(s[7].replace("^", "").replace("~", ""));
                        part[5] = Float.parseFloat(s[8].replace("^", "").replace("~", ""));
                        list.add(part);
                    }
                }
                return list;
            }
        });
    }
}
