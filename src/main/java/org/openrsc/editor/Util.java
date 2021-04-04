package org.openrsc.editor;

import org.openrsc.editor.gui.graphics.EditorCanvas;
import org.openrsc.editor.model.Tile;
import org.openrsc.editor.model.data.GameObjectLoc;
import org.openrsc.editor.model.data.ItemLoc;
import org.openrsc.editor.model.data.NpcLoc;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Util {

	public static int stampSize = 5;
	private static int fpsCount;

	/**
	 * A utility method to find a tile in the grid based on a mouseClick. There
	 * is some overhead due to bounds-checking, but this is done for safety's
	 * sake.
	 *
	 * @param point The mouse click's x/y coordinates.
	 * @return The tile found in {@code tileGrid};
	 */
	public static Tile mapCoordsToGridTile(Point point) throws ArrayIndexOutOfBoundsException {
		return mapCoordsToGridTile(point.x, point.y);
	}

	public static Tile mapCoordsToGridTile(final int x, final int y) throws ArrayIndexOutOfBoundsException {
		if (inCanvas(x, y)) {
			Point tileLocation = mouseCoordsToGridCoords(new Point(x, y));
			try {
				return EditorCanvas.tileGrid[tileLocation.x][tileLocation.y];
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static Point mouseCoordsToGridCoords(Point point) {
		return new Point(
			EditorCanvas.GRID_SIZE - point.x / EditorCanvas.TILE_SIZE - 1,
			point.y / EditorCanvas.TILE_SIZE
		);
	}

	/**
	 * Jumps to the sector and points out the tile of the given coords.
	 */
	public static void handleJumpToCoords() {

		Object temp = JOptionPane.showInputDialog("Enter Coordinates\r\nExample 244,671");
		String[] splitter = temp.toString().split(",");
		int x = Integer.parseInt(splitter[0].trim());
		int y = Integer.parseInt(splitter[1].trim());

		if (x != -1 && y != -1) {
			int sector = 0;
			System.out.println(x + " " + y);
			if (y >= 0 && y <= 1007)
				Util.sectorH = 0;
			else if (y >= 1007 && y <= 1007 + 943) {
				Util.sectorH = 1;
				y -= 943;
			} else if (y >= 1008 + 943 && y <= 1007 + 943 + 943) {
				Util.sectorH = 2;
				y -= 943 * 2;
			} else {
				y -= 943 * 3;
				Util.sectorH = 3;
			}

			Util.sectorX = (x / 48) + 48;
			Util.sectorY = (y / 48) + 37;
			Util.STATE = Util.State.CHANGING_SECTOR;
		}
	}

	// Not used, below. Other method is a tad faster.
	/*
	 * public static Tile findTile(Point p) { for(int i=0; i <
	 * Canvas.tileGrid.length; i++) for(int r=0; r < Canvas.tileGrid[i].length;
	 * r++) if(Canvas.tileGrid[i][r].getShape().getBounds().contains(p)) return
	 * Canvas.tileGrid[i][r]; return null; }
	 */

	public static boolean inCanvas(Point p) {
		return inCanvas(p.x, p.y);
	}

	public static boolean inCanvas(int x, int y) {
		if (!Main.mainWindow.isActive()) {
			return false;
		}
		return x >= 0
			&& y >= 0
			&& x < EditorCanvas.GRID_PIXEL_SIZE + EditorCanvas.TILE_SIZE
			&& y < EditorCanvas.GRID_PIXEL_SIZE;
	}

	/**
	 * Basic sleep method, to stop the CPU from rendering more than needed and
	 * killing the CPU.
	 */
	public static void sleep() {
		try {
			Thread.sleep(THREAD_DELAY);
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Synchronizes the FPS on the thread.
	 */
	public static void syncFps() {
		try {
			fpsCount++;
			if (lastMilli == 0) {
				lastMilli = System.currentTimeMillis();
			}
			if (System.currentTimeMillis() - 1000 > lastMilli) {
				Main.mainWindow.setTitle("RSC Community Landscape Editor" + " - " + " Sector: " + "h" + sectorH + "x"
					+ sectorX + "y" + sectorY);
				fpsCount = 0;
				lastMilli = System.currentTimeMillis();
			}
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * @param e - the Exception thrown. this Method handles all the errors.
	 */
	public static void error(Exception e) {
		System.out.println("Error: " + e.getMessage());
		e.printStackTrace();
		JOptionPane.showMessageDialog(null,
			"a Exception has been thrown\r\nSomething may not be working as expected. \r\n" + e.getMessage()
				+ "\r\n");
		System.out.println(Arrays.toString(e.getStackTrace()));
	}

	/**
	 * @param in - the given BIS
	 * @return - a ByteBuffer loaded with Tile values.
	 * @throws IOException
	 */
	public static ByteBuffer streamToBuffer(BufferedInputStream in) throws IOException {
		byte[] buffer = new byte[in.available()];
		in.read(buffer, 0, buffer.length);
		return ByteBuffer.wrap(buffer);
	}

	/**
	 * Unpack the data from the Landscape file to a ByteBuffer
	 */
	public static void unpack(int sectorX, int sectorY, int sectorH) {
		try {
			tileArchive = new ZipFile(currentFile);
			ZipEntry e = tileArchive.getEntry("h" + sectorH + "x" + sectorX + "y" + sectorY);
			if (e != null) {
				buffer = streamToBuffer(new BufferedInputStream(tileArchive.getInputStream(e)));
			} else {
				JOptionPane.showConfirmDialog(null, "Sorry, Wrong sector String specified.");
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public static ByteBuffer pack() throws IOException {
		ByteBuffer out = ByteBuffer.allocate(10 * (EditorCanvas.GRID_SIZE * EditorCanvas.GRID_SIZE));

		for (int i = 0; i < EditorCanvas.GRID_SIZE; i++) {
			for (int j = 0; j < EditorCanvas.GRID_SIZE; j++) {
				out.put(EditorCanvas.tileGrid[i][j].pack());
			}
		}
		out.flip();
		return out;
	}

	/**
	 * @return If the save was successful or not
	 */
	public static boolean save() {
		ZipFile tileArchive = Util.tileArchive;
		if (tileArchive == null) {
			return false;
		}

		String name = tileArchive.getName();
		try {
			File file = File.createTempFile("darkquest", "land.tmp");
			FileOutputStream dest = new FileOutputStream(file.getPath());

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

			prepareStream(tileArchive, out);
			saveEditedEntry(out);

			out.close();
			dest.close();
			out = null;
			dest = null;

			moveFile(file, new File(name));
			file = null;
			Util.STATE = Util.State.LOADED;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param in  The fileArchive
	 * @param out Temporary stream
	 */
	private static void prepareStream(ZipFile in, ZipOutputStream out) {
		try {
			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) in.entries();

			ZipEntry entry;
			while (entries.hasMoreElements()) {
				entry = entries.nextElement();
				if (entry == null)
					continue;
				if (entry.getName().equalsIgnoreCase("h" + Util.sectorH + "x" + Util.sectorX + "y" + Util.sectorY))
					continue;
				ByteBuffer data = Util.streamToBuffer(new BufferedInputStream(in.getInputStream(entry)));
				writeEntry(out, entry.getName(), data);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param out  The temporary stream to write to
	 * @param name The name of the entry
	 * @param data The data of the entry (a.k.a. map)
	 */
	private static void writeEntry(ZipOutputStream out, String name, ByteBuffer data) {
		try {
			ZipEntry destEntry = new ZipEntry(name);
			out.putNextEntry(destEntry);
			out.write((data.array()), 0, data.remaining());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void saveEditedEntry(ZipOutputStream out) {
		try {
			String name = "h" + Util.sectorH + "x" + Util.sectorX + "y" + Util.sectorY;
			ByteBuffer data = Util.pack();
			writeEntry(out, name, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void moveFile(File in, File out) {
		int BUFFER = 2048;
		byte[] data = new byte[BUFFER];
		BufferedInputStream origin = null;

		try {
			FileInputStream fi = new FileInputStream(in.getPath());
			origin = new BufferedInputStream(fi, BUFFER);

			FileOutputStream dest = new FileOutputStream(out.getPath());

			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			origin.close();
			dest.close();
			in.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void prepareData() {
		try {
			List<GameObjectLoc> gameObjectLocs = PersistenceManager.load(
				Util.class.getResourceAsStream("/data/SceneryLocs.json"),
				GameObjectLoc.class
			);
			gameObjectLocs.forEach(loc -> sceneryLocationMap.put(loc.getLocation(), loc));

			List<GameObjectLoc> boundaryLocs = PersistenceManager.load(
				Util.class.getResourceAsStream("/data/BoundaryLocs.json"),
				GameObjectLoc.class
			);
			boundaryLocs.forEach(loc -> boundaryLocsMap.put(loc.getLocation(), loc));

			List<NpcLoc> npcLocs = PersistenceManager.load(
				Util.class.getResourceAsStream("/data/NpcLocs.json"),
				NpcLoc.class
			);
			npcLocs.forEach(loc -> npcLocationMap.put(loc.getStart(), loc));

			List<ItemLoc> itemLocs = PersistenceManager.load(
				Util.class.getResourceAsStream("/data/GroundItems.json"),
				ItemLoc.class
			);
			itemLocs.forEach(loc -> itemLocationMap.put(loc.getLocation(), loc));

			// Getting all the IDs - names, for objects/npcs/items in the
			// hashmaps.
			Scanner itemNameScanner = new Scanner(Util.class.getResourceAsStream("/xml/item.txt"));
			while (itemNameScanner.hasNextLine()) {
				String[] temp = itemNameScanner.nextLine().split(": ");
				itemNames.put(Integer.valueOf(temp[0]), temp[1]);
			}
			itemNameScanner.close();

			Scanner npcNameScanner = new Scanner(Util.class.getResourceAsStream("/xml/npc.txt"));
			while (npcNameScanner.hasNextLine()) {
				String[] temp = npcNameScanner.nextLine().split(": ");
				npcNames.put(Integer.valueOf(temp[0]), temp[1]);
			}
			npcNameScanner.close();

			Scanner objectNameScanner = new Scanner(Util.class.getResourceAsStream("/xml/objects.txt"));
			while (objectNameScanner.hasNextLine()) {
				String[] temp = objectNameScanner.nextLine().split(": ");
				objectNames.put(Integer.valueOf(temp[0]), temp[1]);
			}
			objectNameScanner.close();
		} catch (Exception e) {
			error(e);
		}
	}

	public static String[] getSectionNames() {
		try {
			tileArchive = new ZipFile(currentFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (tileArchive == null)
			return new String[0];

		String[] sections = new String[tileArchive.size()];

		try {
			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) tileArchive.entries();
			ZipEntry entry = null;

			int i = 0;
			while (entries.hasMoreElements()) {
				entry = entries.nextElement();
				if (entry == null)
					continue;

				sections[i] = entry.getName();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] temp = new String[sections.length];
		int x = 0;
		for (int i = sections.length - 1; i > 0; i--) {
			temp[x] = sections[i];
			x++;
		}
		return temp;
	}

	/**
	 * Sets the HashMaps with the correct values.
	 */

	/**
	 * @param t - the given Tile
	 * @return - the RSC Coordinate for the Tile. Also counts for
	 * underground/upstairs/second story.
	 */
	public static Point getRSCCoords(Tile t) {
		return new Point(
			(t.getGridX() + (Util.sectorX - 48) * 48),
			((((Util.sectorY - 36) * 48) + t.getGridY() + 96) - 144) + (Util.sectorH * 944)
		);
	}

	public static Polygon constructPolygon(List<Point> points) {
		// Construct polygon
		int nPoints = points.size();
		int[] allX = new int[nPoints];
		int[] allY = new int[nPoints];
		for (int i = 0; i < nPoints; i++) {
			Point point = points.get(i);
			allX[i] = point.x;
			allY[i] = point.y;
		}
		return new Polygon(allX, allY, nPoints);
	}

	/**
	 * the State of the Map editor.
	 */
	public enum State {
		NOT_LOADED, LOADED, RENDER_READY, CHANGING_SECTOR, TILE_NEEDS_UPDATING, FORCE_FULL_RENDER
	}

	public static Map<Integer, String> objectNames = new HashMap<>();
	public static Map<Integer, String> npcNames = new HashMap<>();
	public static Map<Integer, String> itemNames = new HashMap<>();
	public static Map<Point, ItemLoc> itemLocationMap = new HashMap<>();
	public static Map<Point, NpcLoc> npcLocationMap = new HashMap<>();
	public static Map<Point, GameObjectLoc> sceneryLocationMap = new HashMap<>();
	public static Map<Point, GameObjectLoc> boundaryLocsMap = new HashMap<>();

	public static State STATE = State.NOT_LOADED;
	private static long lastMilli = 0;
	public static int sectorX = 51;
	public static int sectorY = 50;
	public static int sectorH = 0;
	public static boolean sectorModified = false;
	public static final int THREAD_DELAY = 8;

	public static ByteBuffer buffer;
	public static ZipFile tileArchive;
	public static File currentFile = null;
	public static boolean MAP_BRIGHTNESS_LIGHT = false;

	/**
	 * The array that holds all the RGB colors for each Tile's groundTexture
	 * value.
	 */
	public static final Color[] colorArray = {new Color(255, 255, 255), new Color(251, 254, 251), new Color(247, 252, 247),
		new Color(243, 250, 243), new Color(239, 248, 239), new Color(235, 247, 235), new Color(231, 245, 231),
		new Color(227, 243, 227), new Color(223, 241, 223), new Color(219, 240, 219), new Color(215, 238, 215),
		new Color(211, 236, 211), new Color(207, 234, 207), new Color(203, 233, 203), new Color(199, 231, 199),
		new Color(195, 229, 195), new Color(191, 227, 191), new Color(187, 226, 187), new Color(183, 224, 183),
		new Color(179, 222, 179), new Color(175, 220, 175), new Color(171, 219, 171), new Color(167, 217, 167),
		new Color(163, 215, 163), new Color(159, 213, 159), new Color(155, 212, 155), new Color(151, 210, 151),
		new Color(147, 208, 147), new Color(143, 206, 143), new Color(139, 205, 139), new Color(135, 203, 135),
		new Color(131, 201, 131), new Color(127, 199, 127), new Color(123, 198, 123), new Color(119, 196, 119),
		new Color(115, 194, 115), new Color(111, 192, 111), new Color(107, 191, 107), new Color(103, 189, 103),
		new Color(99, 187, 99), new Color(95, 185, 95), new Color(91, 184, 91), new Color(87, 182, 87),
		new Color(83, 180, 83), new Color(79, 178, 79), new Color(75, 177, 75), new Color(71, 175, 71),
		new Color(67, 173, 67), new Color(63, 171, 63), new Color(59, 170, 59), new Color(55, 168, 55),
		new Color(51, 166, 51), new Color(47, 164, 47), new Color(43, 163, 43), new Color(39, 161, 39),
		new Color(35, 159, 35), new Color(31, 157, 31), new Color(27, 156, 27), new Color(23, 154, 23),
		new Color(19, 152, 19), new Color(15, 150, 15), new Color(11, 149, 11), new Color(7, 147, 7),
		new Color(3, 145, 3), new Color(0, 144, 0), new Color(3, 144, 0), new Color(6, 144, 0),
		new Color(9, 144, 0), new Color(12, 144, 0), new Color(15, 144, 0), new Color(18, 144, 0),
		new Color(21, 144, 0), new Color(24, 144, 0), new Color(27, 144, 0), new Color(30, 144, 0),
		new Color(33, 144, 0), new Color(36, 144, 0), new Color(39, 144, 0), new Color(42, 144, 0),
		new Color(45, 144, 0), new Color(48, 144, 0), new Color(51, 144, 0), new Color(54, 144, 0),
		new Color(57, 144, 0), new Color(60, 144, 0), new Color(63, 144, 0), new Color(66, 144, 0),
		new Color(69, 144, 0), new Color(72, 144, 0), new Color(75, 144, 0), new Color(78, 144, 0),
		new Color(81, 144, 0), new Color(84, 144, 0), new Color(87, 144, 0), new Color(90, 144, 0),
		new Color(93, 144, 0), new Color(96, 144, 0), new Color(99, 144, 0), new Color(102, 144, 0),
		new Color(105, 144, 0), new Color(108, 144, 0), new Color(111, 144, 0), new Color(114, 144, 0),
		new Color(117, 144, 0), new Color(120, 144, 0), new Color(123, 144, 0), new Color(126, 144, 0),
		new Color(129, 144, 0), new Color(132, 144, 0), new Color(135, 144, 0), new Color(138, 144, 0),
		new Color(141, 144, 0), new Color(144, 144, 0), new Color(147, 144, 0), new Color(150, 144, 0),
		new Color(153, 144, 0), new Color(156, 144, 0), new Color(159, 144, 0), new Color(162, 144, 0),
		new Color(165, 144, 0), new Color(168, 144, 0), new Color(171, 144, 0), new Color(174, 144, 0),
		new Color(177, 144, 0), new Color(180, 144, 0), new Color(183, 144, 0), new Color(186, 144, 0),
		new Color(189, 144, 0), new Color(192, 144, 0), new Color(191, 143, 0), new Color(189, 141, 0),
		new Color(188, 140, 0), new Color(186, 138, 0), new Color(185, 137, 0), new Color(183, 135, 0),
		new Color(182, 134, 0), new Color(180, 132, 0), new Color(179, 131, 0), new Color(177, 129, 0),
		new Color(176, 128, 0), new Color(174, 126, 0), new Color(173, 125, 0), new Color(171, 123, 0),
		new Color(170, 122, 0), new Color(168, 120, 0), new Color(167, 119, 0), new Color(165, 117, 0),
		new Color(164, 116, 0), new Color(162, 114, 0), new Color(161, 113, 0), new Color(159, 111, 0),
		new Color(158, 110, 0), new Color(156, 108, 0), new Color(155, 107, 0), new Color(153, 105, 0),
		new Color(152, 104, 0), new Color(150, 102, 0), new Color(149, 101, 0), new Color(147, 99, 0),
		new Color(146, 98, 0), new Color(144, 96, 0), new Color(143, 95, 0), new Color(141, 93, 0),
		new Color(140, 92, 0), new Color(138, 90, 0), new Color(137, 89, 0), new Color(135, 87, 0),
		new Color(134, 86, 0), new Color(132, 84, 0), new Color(131, 83, 0), new Color(129, 81, 0),
		new Color(128, 80, 0), new Color(126, 78, 0), new Color(125, 77, 0), new Color(123, 75, 0),
		new Color(122, 74, 0), new Color(120, 72, 0), new Color(119, 71, 0), new Color(117, 69, 0),
		new Color(116, 68, 0), new Color(114, 66, 0), new Color(113, 65, 0), new Color(111, 63, 0),
		new Color(110, 62, 0), new Color(108, 60, 0), new Color(107, 59, 0), new Color(105, 57, 0),
		new Color(104, 56, 0), new Color(102, 54, 0), new Color(101, 53, 0), new Color(99, 51, 0),
		new Color(98, 50, 0), new Color(96, 48, 0), new Color(95, 49, 0), new Color(93, 51, 0),
		new Color(92, 52, 0), new Color(90, 54, 0), new Color(89, 55, 0), new Color(87, 57, 0),
		new Color(86, 58, 0), new Color(84, 60, 0), new Color(83, 61, 0), new Color(81, 63, 0),
		new Color(80, 64, 0), new Color(78, 66, 0), new Color(77, 67, 0), new Color(75, 69, 0),
		new Color(74, 70, 0), new Color(72, 72, 0), new Color(71, 73, 0), new Color(69, 75, 0),
		new Color(68, 76, 0), new Color(66, 78, 0), new Color(65, 79, 0), new Color(63, 81, 0),
		new Color(62, 82, 0), new Color(60, 84, 0), new Color(59, 85, 0), new Color(57, 87, 0),
		new Color(56, 88, 0), new Color(54, 90, 0), new Color(53, 91, 0), new Color(51, 93, 0),
		new Color(50, 94, 0), new Color(48, 96, 0), new Color(47, 97, 0), new Color(45, 99, 0),
		new Color(44, 100, 0), new Color(42, 102, 0), new Color(41, 103, 0), new Color(39, 105, 0),
		new Color(38, 106, 0), new Color(36, 108, 0), new Color(35, 109, 0), new Color(33, 111, 0),
		new Color(32, 112, 0), new Color(30, 114, 0), new Color(29, 115, 0), new Color(27, 117, 0),
		new Color(26, 118, 0), new Color(24, 120, 0), new Color(23, 121, 0), new Color(21, 123, 0),
		new Color(20, 124, 0), new Color(18, 126, 0), new Color(17, 127, 0), new Color(15, 129, 0),
		new Color(14, 130, 0), new Color(12, 132, 0), new Color(11, 133, 0), new Color(9, 135, 0),
		new Color(8, 136, 0), new Color(6, 138, 0), new Color(5, 139, 0), new Color(3, 141, 0),
		new Color(2, 142, 0)};
}
