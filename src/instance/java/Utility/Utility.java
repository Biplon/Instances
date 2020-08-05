package instance.java.Utility;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Utility
{
    public static void search(final String pattern, final File folder, List<String> result)
    {
        try
        {
            for (final File f : Objects.requireNonNull(folder.listFiles()))
            {
                if (f.isDirectory())
                {
                    search(pattern, f, result);
                }

                if (f.isFile())
                {
                    if (f.getName().matches(pattern))
                    {
                        result.add(f.getAbsolutePath());
                    }
                }
            }
        }
        catch (Exception ignored)
        {

        }
    }
}
