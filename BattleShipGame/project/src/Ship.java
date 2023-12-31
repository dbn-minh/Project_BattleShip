import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;

public class Ship {
    private static final Map<Integer, Image> shipImages = loadShipImages();

    private static Map<Integer, Image> loadShipImages() {
        Map<Integer, Image> images = new HashMap<>();
        // Load images for each BoatSize
        // Example: (You need to provide the actual file paths)
        images.put(2, new ImageIcon("BattleShipGame/project/Image/PlaneF-35Lightning2.png").getImage());
        images.put(3, new ImageIcon("BattleShipGame/project/Image/submarine.jpg").getImage());
        images.put(4, new ImageIcon("BattleShipGame/project/Image/cruiser.jpg").getImage());
        images.put(5, new ImageIcon("BattleShipGame/project/Image/ShipCarrierHull.png").getImage());

        // Add more images for different BoatSizes
        return images;
    }

    private Image shipImage;

    public enum ShipPlacementColour {
        Valid, Invalid, Placed
    }

    /**
     * The position in grid coordinates for where the ship is located.
     */
    private Position gridPosition;
    /**
     * The position in pixels for drawing the ship.
     */
    private Position drawPosition;
    /**
     * The number of segments in the ship to show how many cells it goes across.
     */
    private int segments;
    /**
     * True indicates the ship is horizontal, and false indicates the ship is
     * vertical.
     */
    private boolean isSideways;
    /**
     * The number of destroyed sections to help determine if all of the ship has
     * been destroyed when compared to segments.
     */
    private int destroyedSections;
    /**
     * Used to change the colour during manual placement by the player to show Green
     * or Red to show valid and invalid placement.
     */
    private ShipPlacementColour shipPlacementColour;

    /**
     * Creates the ship with default properties ready for use. Assumes it has
     * already been placed when created.
     *
     * @param gridPosition The position where the ship is located in terms of grid
     *                     coordinates.
     * @param drawPosition Top left corner of the cell to start drawing the ship in
     *                     represented in pixels.
     * @param segments     The number of segments in the ship to show how many cells
     *                     it goes across.
     * @param isSideways   True indicates the ship is horizontal, and false
     *                     indicates the ship is vertical.
     */
    public Ship(Position gridPosition, Position drawPosition, int segments, boolean isSideways) {
        this.gridPosition = gridPosition;
        this.drawPosition = drawPosition;
        this.segments = segments;
        this.isSideways = isSideways;
        destroyedSections = 0;
        shipPlacementColour = ShipPlacementColour.Placed;

        // ImageIcon imageIcon1 = new ImageIcon("C:\\work\\k23\\DSA
        // project\\hinh\\PatrolBoat\\ShipPatrolHullvertical.png");
        // ImageIcon imageIcon = new ImageIcon(
        // "C:\\work\\k23\\DSA project\\hinh\\Cruiser\\ShipCruiserHull.png");
        // shipImage = imageIcon.getImage();

        shipImage = shipImages.get(segments);
    }

    /**
     * Draws the ship by first selecting the colour and then drawing the ship in the
     * correct direction.
     * Colour is selected to be: Green if currently placing and it is valid, red if
     * it is placing and invalid.
     * If it has already been placed it will show as red if destroyed, or dark gray
     * in any other case.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        if (shipPlacementColour == ShipPlacementColour.Placed) {
            g.setColor(destroyedSections >= segments ? Color.black : Color.DARK_GRAY);
        } else {
            g.setColor(shipPlacementColour == ShipPlacementColour.Valid ? new Color(19, 122, 35) : Color.RED);
        }
        if (isSideways)
            paintHorizontal(g);
        else
            paintVertical(g);

        // if (shipImage != null) {
        // // Draw the ship image
        // g.drawImage(shipImage, drawPosition.x, drawPosition.y, null);
        // }

    }

    /**
     * Sets the placement colour to indicate the state of the ship.
     *
     * @param shipPlacementColour Valid sets ship to show Green, Invalid sets ship
     *                            to show Red, Placed sets to defaults.
     */
    public void setShipPlacementColour(ShipPlacementColour shipPlacementColour) {
        this.shipPlacementColour = shipPlacementColour;
    }

    /**
     * Toggles the current state between vertical and horizontal.
     */
    public void toggleSideways() {
        isSideways = !isSideways;
    }

    /**
     * Call when a section has been destroyed to let the ship keep track of how many
     * sections have been destroyed.
     */
    public void destroySection() {
        destroyedSections++;
    }

    /**
     * Tests if the number of sections destroyed indicate all segments have been
     * destroyed.
     *
     * @return True if all sections have been destroyed.
     */
    public boolean isDestroyed() {
        return destroyedSections >= segments;
    }

    /**
     * Updates the position to draw the ship at to the newPosition.
     *
     * @param gridPosition Position where the ship is now on the grid.
     * @param drawPosition Position to draw the Ship at in Pixels.
     */
    public void setDrawPosition(Position gridPosition, Position drawPosition) {
        this.drawPosition = drawPosition;
        this.gridPosition = gridPosition;
    }

    /**
     * Gets the current direction of the ship.
     *
     * @return True if the ship is currently horizontal, or false if vertical.
     */
    public boolean isSideways() {
        return isSideways;
    }

    /**
     * Gets the number of segments that make up the ship.
     *
     * @return The number of cells the ship occupies.
     */
    public int getSegments() {
        return segments;
    }

    /**
     * Gets a list of all cells that this ship occupies to be used for validation in
     * AI checks.
     *
     * @return A list of all cells that this ship occupies.
     */
    public List<Position> getOccupiedCoordinates() {
        List<Position> result = new ArrayList<>();
        if (isSideways) { // handle the case when horizontal
            for (int x = 0; x < segments; x++) {
                result.add(new Position(gridPosition.x + x, gridPosition.y));
            }
        } else { // handle the case when vertical
            for (int y = 0; y < segments; y++) {
                result.add(new Position(gridPosition.x, gridPosition.y + y));
            }
        }
        return result;
    }

    // Helper method to rotate a BufferedImage
    private Image rotateImage(BufferedImage image, double radians) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage rotatedImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        // Perform the rotation
        g2d.rotate(radians, height / 2, width / 2);
        g2d.drawImage(image, (height - width) / 2, (width - height) / 2, null);
        g2d.dispose();

        return rotatedImage;
    }

    public void paintVertical(Graphics g) {
        int boatWidth = (int) (SelectionGrid.CELL_SIZE * 0.8);
        int boatLeftX = drawPosition.x + SelectionGrid.CELL_SIZE / 2 - boatWidth / 2;

        if (shipImage != null) {
            // Convert Image to BufferedImage
            BufferedImage bufferedImage = new BufferedImage(shipImage.getWidth(null), shipImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(shipImage, 0, 0, null);
            g2d.dispose();

            // Create a rotated copy of the ship image
            Image rotatedImage = rotateImage(bufferedImage, Math.PI / 2); // Rotate 90 degrees

            // Vẽ hình ảnh lên hình chữ nhật
            g.drawImage(rotatedImage, boatLeftX, drawPosition.y + SelectionGrid.CELL_SIZE / 2, boatWidth,
                    (int) (SelectionGrid.CELL_SIZE * (segments - 1.2) + 30), null);

        }
    }

    public void paintHorizontal(Graphics g) {
        int boatWidth = (int) (SelectionGrid.CELL_SIZE * 0.8);
        int boatTopY = drawPosition.y + SelectionGrid.CELL_SIZE / 2 - boatWidth / 2;

        if (shipImage != null) {
            g.drawImage(shipImage, drawPosition.x + SelectionGrid.CELL_SIZE / 4, boatTopY,
                    (int) (SelectionGrid.CELL_SIZE * (segments - 1.2) + 30),
                    boatWidth, null);
        }
    }
}
