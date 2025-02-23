package top.superxuqc.mcmod.common.particle;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.io.*;

public class ConvertArgUtil {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\X\\Desktop\\out\\18.mcfunction");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("particle minecraft:dust")) {
                String[] s = line.split(" ");
                System.out.println("{" + s[2] + "f, " + s[3] + "f, " + s[4] + "f, "
                        + s[6].replace("~", "").replace("^", "") + "f, "
                        + s[7].replace("~", "").replace("^", "") + "f, "
                        + s[8].replace("~", "").replace("^", "") + "f}, ");
            }
        }
        System.out.println(calVelocity(-90, 0));
    }

    public static Vec3d calVelocity(float pitch, float yaw) {
        float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        float g = -MathHelper.sin((pitch) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        Vec3d vec3d = new Vec3d(f, g, h);
        return vec3d;
    }
}
