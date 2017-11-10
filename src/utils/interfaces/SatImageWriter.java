package utils.interfaces;

import java.io.IOException;

public interface SatImageWriter
{

    /**
     * <p>将Writer内的SatImage写入路径</p>
     * <p>Writes the Image stored in the write to a specified file.</p>
     *
     * @param filepath
     */
    void write(String filepath) throws IOException;

}
