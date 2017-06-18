package metifikys.bin.to.image.converter;

import java.io.IOException;

/**
 * Created by Metifikys on 16.06.2017.
 */
public class Main {

    public static void main(String[] args) {
//        Reader r = new Reader("D:\\1.zip");
        Reader r = new Reader("D:\\Soft2\\FileZilla Server.rar");
//        Reader r = new Reader("D:\\Soft2\\Erlang.636.zip");

        try {
            r.process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}