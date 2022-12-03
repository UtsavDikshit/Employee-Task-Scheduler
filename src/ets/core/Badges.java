package ets.core;

import javax.swing.ImageIcon;

public class Badges {

    public static String getBadgeCurrent(long score) {
        if (score <= 20) {
            return "Bronze";
        } else if (score <= 100) {
            return "Silver";
        } else if (score <= 200) {
            return "Gold";
        } else {
            return "Diamond";
        }
    }

    public static ImageIcon getBadgeIcon(String badge) {
        switch (badge) {
            case "Bronze":
                return new ImageIcon("badges/bronze.png");
            case "Silver":
                return new ImageIcon("badges/silver.png");
            case "Gold":
                return new ImageIcon("badges/gold.png");
            case "Diamond":
                return new ImageIcon("badges/diamond.png");
        }
        return null;
    }

}
