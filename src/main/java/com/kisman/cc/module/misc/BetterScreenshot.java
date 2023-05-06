package com.kisman.cc.module.misc;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.util.process.file.ClipboardImage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author BloomWareClient
 */
public class BetterScreenshot extends Module {
    public static BetterScreenshot instance;

    public BetterScreenshot() {
        super("BetterScreenshot", Category.MISC);

        instance = this;
    }

    public static Image getLatestScreenshot() throws IOException {
        try (Stream<Path> files = Files.list((new File(mc.gameDir.getAbsolutePath() + "/screenshots/")).toPath())) {
            Optional<Path> latestScreenshot = files
                    .filter(file -> !Files.isDirectory(file))
                    .max(Comparator.comparingLong(file -> file.toFile().lastModified()));

            return latestScreenshot.map(path -> new ImageIcon(path.toString()).getImage()).orElse(null);
        }
    }

    public static void copyToClipboard(Image image) {
        new Thread(() -> Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ClipboardImage(image), null)).start();
    }
}
