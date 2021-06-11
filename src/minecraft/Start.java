import java.io.File;
import java.util.Arrays;

import net.minecraft.client.main.Main;

public class Start
{
    public static void main(String[] args)
    {
        final File file = new File(new File(System.getenv("APPDATA")), ".minecraft");
        Main.main((String[])concat(new String[] {"--version", "Aero Client", "--accessToken",                                                                                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MGY1MzJkNGU1ZDhkZTAxOTI2OTQ2ZGRlMWIwMjZlOCIsInlnZ3QiOiJmNTg3YTYyMTBmNzE0YzkxYTJlOWE0YjY4OTU5MTY3ZCIsInNwciI6IjU4MDI1MTI2MTdjMjRmZTNiYmM5ODE1MTkzNDFmN2Q2IiwiaXNzIjoiWWdnZHJhc2lsLUF1dGgiLCJleHAiOjE2MTIwNDI0NjYsImlhdCI6MTYxMTg2OTY2Nn0.zOsM3ihFY_P9w81fIzvHglaiVkX4jmA22-8vK4N0sBs", "--assetIndex", "" +
                "1.8.9", "--userProperties", "{}", "--gameDir", file.getAbsolutePath(), "--assetsDir",
                new File(file, "assets").getAbsolutePath(), "--username", "ArcaneCC", "--uuid", "58025126-17c2-4fe3-bbc9-81519341f7d6"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
