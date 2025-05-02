
package org.ps5jb.loader.jar.menu;

import org.ps5jb.loader.Config;
import org.ps5jb.loader.KernelReadWrite;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Ps5MenuLoader {
    private int selected = 1;
    private int selectedSub = 1;
    private boolean subMenuActive = false;

    private final Ps5MenuItem[] menuItems;
    private Map submenuItems = new HashMap();

    private int scrollX = Config.getLoaderResolutionWidth();
    private int scrollXCounter = 0;

    private TextScroller scroller1 = new TextScroller("                  Select Pipeline runner and click on first file, load all from Disc or Disc JAR Loader and follow steps 1-3 to run it manually", 50, 280, 0, false);
    private TextScroller scroller2 = new TextScroller("               If you choose one of the USB Pipeline runner , check files on USB and renamed like this --> elfloader.jar etaHEN.bin kstuff.elf", 50, 330, 0, true);

    public Ps5MenuLoader(final Ps5MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    public void renderMenu(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Config.getLoaderResolutionWidth(), Config.getLoaderResolutionHeight());

        renderIcons(g2d);
        scroller1.update();
        scroller2.update();
        g2d.setColor(Color.WHITE);
        scroller1.draw(g2d);
        g2d.setColor(Color.WHITE);
        scroller2.draw(g2d);
    }

    private void renderIcons(final Graphics2D g2d) {
        final int iconSpacing = 370;
        final int iconTopY = 440;
        int nextX = iconSpacing;

        for (int i = 0; i < menuItems.length; i++) {
            final Ps5MenuItem item = menuItems[i];

            g2d.drawImage(item.getIcon(), nextX, iconTopY, null);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Sans", Font.BOLD, 20));
            g2d.drawString(item.getLabel(), (int)(nextX + ((256 / 2f) - item.getLabel().length() * 4.5f)), iconTopY + 256 + 30);

            if (i + 1 == selected && subMenuActive) {
                int nextY = 0;
                Ps5MenuItem[] subItems = (Ps5MenuItem[]) submenuItems.get(String.valueOf(i + 1));
                if (subItems != null && subItems.length > 0) {
                    for (int j = 0; j < subItems.length; j++) {
                        final Ps5MenuItem subItem = subItems[j];

                        if (getSelectedSub() - 1 == j) {
                            g2d.setColor(Color.WHITE);
                            g2d.setFont(new Font("Sans", Font.BOLD, 25));
                            g2d.drawString("> " + subItem.getLabel() + " <", nextX, iconTopY + 256 + 30 + 50 + nextY);
                        } else {
                            g2d.setColor(Color.WHITE);
                            g2d.setFont(new Font("Sans", Font.PLAIN, 25));
                            g2d.drawString(subItem.getLabel(), nextX, iconTopY + 256 + 30 + 50 + nextY);
                        }
                        nextY += 35;
                    }
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Sans", Font.PLAIN, 25));
                    g2d.drawString("Not available!", nextX, iconTopY + 256 + 30 + 50);
                }
            }

            nextX += 256 + 50;
        }

        g2d.setColor(new Color(64, 156, 217, 51));
        g2d.fillRoundRect(iconSpacing - 10 + (selected - 1) * (256 + 50), iconTopY - 10, 256 + 20, 256 + 50, 40, 40);

        g2d.setFont(new Font("Monospaced", Font.BOLD, 30));
        String kernelText = KernelReadWrite.hasAccessorState() ? "Kernel R/W available!" : "Kernel R/W not available!";
        int kernelWidth = g2d.getFontMetrics().stringWidth(kernelText);
        g2d.setColor(KernelReadWrite.hasAccessorState() ? Color.GREEN : Color.RED);
        g2d.drawString(kernelText, (Config.getLoaderResolutionWidth() - kernelWidth) / 2, 80);

        String pressText = "Press X to select menu entry.";
        int pressWidth = g2d.getFontMetrics().stringWidth(pressText);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(pressText, (Config.getLoaderResolutionWidth() - pressWidth) / 2, 130);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, Config.getLoaderResolutionHeight() - 60, Config.getLoaderResolutionWidth(), 40);
        g2d.setColor(KernelReadWrite.hasAccessorState() ? Color.GREEN : Color.RED);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 30));

        String bounceText = "thanks to all the developers, programmers, idea providers and everyone involved in the PS5 scene. i can't mention everyone by name because i don't know if anyone wants to. modmenu by DriveRick";
        int bounceTextWidth = g2d.getFontMetrics().stringWidth(bounceText);
        int bounceY = (int)(Math.sin(scrollX * 0.03) * 6);
        int y = Config.getLoaderResolutionHeight() - 60;
        g2d.drawString(bounceText, scrollX, y + bounceY);

        if (scrollXCounter % 2 == 0) scrollX -= 1;
        scrollXCounter++;
        if (scrollX + bounceTextWidth < 0) {
            scrollX = Config.getLoaderResolutionWidth();
        }
    }

    public int getSelected() { return selected; }
    public void setSelected(int selected) { this.selected = selected; }
    public int getSelectedSub() { return selectedSub; }
    public void setSelectedSub(int selectedSub) { this.selectedSub = selectedSub; }
    public boolean isSubMenuActive() { return subMenuActive; }
    public void setSubMenuActive(boolean subMenuActive) {
        this.subMenuActive = subMenuActive;
        this.setSelectedSub(1);
    }
    public Ps5MenuItem[] getMenuItems() { return menuItems; }
    public Ps5MenuItem[] getSubmenuItems(int mainItemId) {
        return (Ps5MenuItem[]) submenuItems.get(String.valueOf(mainItemId));
    }
    public void setSubmenuItems(int mainItemId, Ps5MenuItem[] submenuItems) {
        this.submenuItems.put(String.valueOf(mainItemId), submenuItems);
    }
}
