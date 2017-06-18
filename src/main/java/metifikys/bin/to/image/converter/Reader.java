package metifikys.bin.to.image.converter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Metifikys on 16.06.2017.
 */
public class Reader {

    private String fileName;

    public Reader(String fileName) {
        this.fileName = fileName;
    }

    public void process() throws IOException {

        File f = new File(fileName);
        InputStream in = Files.newInputStream(f.toPath());

        int y = 2;
        int x = (int)(f.length() / y);

        long time = System.currentTimeMillis();
        List<Codrs> list = new ArrayList<Codrs>();
        for (int i = y; i < x; i++) {
            int l = (int)f.length() / i;
            int delta = (int)f.length() - l * i;

            if (delta < 10) {
                list.add(new Codrs((l + delta), i, delta, Math.abs((l + delta) - i)));
            }
        }
        Collections.sort(list);

/*        List<Codrs> list = IntStream.range(y, x)
                .parallel()
                .mapToObj(v -> {
                    int l = (int)f.length() / v;
                    int delta = (int)f.length() - l * v;

                    return new Codrs((l + delta), v, delta, Math.abs((l + delta) - v));
                })
//                .filter(codrs -> codrs.delta < 10)
                .sorted()
                .limit(2)
                .collect(Collectors.toList());*/


        System.out.println(System.currentTimeMillis() - time);
/*        list.forEach(
                System.out::println
        );*/

        y = list.get(0).y;
        x = list.get(0).x + list.get(0).delta;

//        x += f.length() - x * y;
        BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);

        int globalIndexX = 0;
        int globalIndexY = 0;

        byte[] buffer = new byte[1024 * 50];
        int read = in.read(buffer);
        while (read > 0){

            for (int i = 0; i < read; i++) {
//                System.out.println((globalIndexX + 1) * (globalIndexY + 1));
                image.setRGB(globalIndexX++, globalIndexY, buffer[i]);

                if (globalIndexX == x){
                    globalIndexX = 0;
                    ++globalIndexY;
                }
            }

            read = in.read(buffer);
        }

        ImageIO.write(image, "PNG", new File(f.getAbsolutePath() + ".png"));
    }

    private static class Codrs implements Comparable<Codrs>{
        int x;
        int y;
        int delta;
        int razn;

        public Codrs(int x, int y, int delta, int razn) {
            this.x = x;
            this.y = y;
            this.delta = delta;
            this.razn = razn;
        }

        @Override
        public String toString() {
            return "Codrs{" +
                    "x=" + x +
                    ", y=" + y +
                    ", delta=" + delta +
                    ", razn=" + razn +
                    '}';
        }

        public int compareTo(Codrs o) {

            int compare = Integer.compare(delta, o.delta);

            if (compare == 0){
                compare = Integer.compare(razn, o.razn);
            }

            return compare;
        }
    }
}
