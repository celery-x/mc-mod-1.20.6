package top.superxuqc.mcmod.common.particle;

import java.io.*;

public class ConvertArgUtil {
    public static void main(String[] args) throws IOException {
        File file = new File("D://121.mcfunction");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("particle minecraft:dust")) {
                String[] s = line.split(" ");
                System.out.println("{" + s[2] + "f, " + s[3] + "f, " + s[4] + "f, "
                        + s[6].replace("~", "") + "f, "
                        + s[7].replace("~", "") + "f, "
                        + s[8].replace("~", "") + "f}, ");
            }
        }
    }
}
