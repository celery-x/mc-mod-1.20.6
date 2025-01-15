package top.superxuqc.mcmod.common;

import net.minecraft.util.math.MathHelper;

public class RendererUtil {

    public static double caculateAngle(double oriX,  double oriY, double oriZ, double x, double y, double z) {
        double dx = MathHelper.sqrt((float) (MathHelper.square(x - oriX) + MathHelper.square(z - oriZ)));
        double dy = y - oriY;

        double v = MathHelper.atan2(dy, dx);

        return Math.toDegrees(v);

    }

}
