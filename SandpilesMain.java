package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class SandpilesMain {
	static int colors[];
	static AWTSequenceEncoder8Bit encoder;
	static int[][] pile;
	static ArrayList<ArrayList<Integer>> unbounded;

	public static void main(String[] args) throws IOException {
		System.out.println("I dont feel like checking for variable mismatch so dont enter the wrong types, thanks.");
		boolean isUnstable = true;
		int numCollapse = 0;
		File file = new File("sandpiles.mp4");
		System.out.println("Enter the frame rate as an int. Higher number is a faster animation.");
		Scanner inputs = new Scanner(System.in);
		int framerate = inputs.nextInt();
		long startTime = System.nanoTime();
		encoder = AWTSequenceEncoder8Bit.createSequenceEncoder8Bit(file, framerate);
		setColorPalete();
		setGrid();
		while (isUnstable) {
			numCollapse++;
			System.out.println(numCollapse);
			setFrame(pile);
			isUnstable = false;
			pile = collapse(pile);
			for (int i = 0; i < pile.length; i++) {
				for (int k = 0; k < pile[0].length; k++) {
					if (pile[i][k] > 3) {
						isUnstable = true;
					}
				}
			}
		}
		BufferedImage finalImage = new BufferedImage(pile.length, pile[0].length, BufferedImage.TYPE_INT_BGR);
		for (int i = 0; i < pile.length; i++) {
			for (int k = 0; k < pile[0].length; k++) {
				finalImage.setRGB(i, k, colors[pile[i][k]]);
			}
		}
		ImageIO.write(finalImage, "png", new File("finalFrame.png"));
		encoder.finish();
		System.out.println("Number of Colapses: " + numCollapse);
		System.out.println((System.nanoTime() - startTime) / 1000000000 + " seconds");
	}

	public static void setColorPalete() {
		colors = new int[8];
		colors[0] = new Color(0, 0, 0).getRGB();
		colors[1] = new Color(0, 255, 128).getRGB();
		colors[2] = new Color(0, 166, 255).getRGB();
		colors[3] = new Color(64, 0, 255).getRGB();
		colors[4] = new Color(255, 0, 230).getRGB();
		colors[5] = new Color(255, 0, 13).getRGB();
		colors[6] = new Color(255, 204, 0).getRGB();
		colors[7] = new Color(89, 255, 0).getRGB();
	}

	public static void setFrame(int[][] pile) throws IOException {
		BufferedImage frame = new BufferedImage(pile.length, pile[0].length, BufferedImage.TYPE_INT_BGR);
		for (int i = 0; i < pile.length; i++) {
			for (int k = 0; k < pile[0].length; k++) {
				frame.setRGB(i, k, colors[pile[i][k]]);
			}
		}
		encoder.encodeImage(frame);
	}

	public static void setGrid() {
		Scanner inputs = new Scanner(System.in);
		System.out.println("Enter the width of your grid as an int");
		int width = inputs.nextInt();
		System.out.println("Enter the height of your grid as an int");
		int height = inputs.nextInt();
		pile = new int[width][height];
		System.out.println("Enter 1 for random or 2-5 for a preset one.");
		int type = inputs.nextInt();
		if (type == 1) {
			System.out.println("enter base number to fill the grid with(0-3)");
			int base = inputs.nextInt();
			for (int i = 0; i < pile.length; i++) {
				for (int k = 0; k < pile.length; k++) {
					pile[i][k] = base;
				}
			}
			System.out.println("enter number of random sand to drop");
			int numRand = inputs.nextInt();
			for (int i = 0; i < numRand; i++) {
				pile[(int) (Math.random() * pile.length)][(int) (Math.random() * pile[0].length)] = (int) (Math.random()
						* 8);
			}
			System.out.println("random done");
		} else if (type == 2) {
			for (int i = 0; i < pile.length; i++) {
				for (int k = 0; k < pile[0].length; k++) {
					pile[i][k] = 3;
				}
				pile[0][0] = 7;
				pile[pile.length - 1][0] = 7;
				pile[0][pile[0].length - 1] = 7;
				pile[pile.length - 1][pile[0].length - 1] = 7;
				pile[pile.length / 2][pile[0].length / 2] = 7;
				pile[pile.length / 2 - 1][pile[0].length / 2] = 7;
				pile[pile.length / 2][pile[0].length / 2 - 1] = 7;
				pile[pile.length / 2 - 1][pile[0].length / 2 - 1] = 7;
			}
		} else if (type == 3) {
			for (int i = 0; i < pile.length; i++) {
				for (int k = 0; k < pile[0].length; k++) {
					pile[i][k] = 3;
				}
				pile[pile.length / 2][pile[0].length / 2] = 7;
				pile[pile.length / 2 - 1][pile[0].length / 2] = 7;
				pile[pile.length / 2][pile[0].length / 2 - 1] = 7;
				pile[pile.length / 2 - 1][pile[0].length / 2 - 1] = 7;
			}
		} else if (type == 4) {
			for (int i = 0; i < pile.length; i++) {
				for (int k = 0; k < pile[0].length; k++) {
					pile[i][k] = 3;
				}
			}
			for (int i = 0; i < pile.length; i++) {
				for (int k = 0; k < pile[0].length; k++) {
					if (i == 0 || i == pile.length - 1 || k == 0 || k == pile[0].length - 1)
						pile[i][k] = 7;
				}
			}
			pile[pile.length / 2][pile[0].length / 2] = 7;
			pile[pile.length / 2 - 1][pile[0].length / 2] = 7;
			pile[pile.length / 2][pile[0].length / 2 - 1] = 7;
			pile[pile.length / 2 - 1][pile[0].length / 2 - 1] = 7;
		} else if (type == 5) {
			for (int i = 0; i < pile.length; i++) {
				for (int k = 0; k < pile[0].length; k++) {
					pile[i][k] = 7;
				}
			}
		}
	}

	public static int[][] collapse(int[][] unstable) {
		int swap[][] = new int[unstable.length][unstable[0].length];
		for (int i = 0; i < unstable.length; i++) {
			for (int k = 0; k < unstable[0].length; k++) {
				swap[i][k] = unstable[i][k];
			}
		}
		for (int i = 0; i < unstable.length; i++) {
			for (int k = 0; k < unstable[0].length; k++) {
				if (unstable[i][k] > 3) {
					swap[i][k] -= 4;
					if (i - 1 >= 0) {
						swap[i - 1][k]++;
					}
					if (i + 1 < unstable.length) {
						swap[i + 1][k]++;
					}
					if (k - 1 >= 0) {
						swap[i][k - 1]++;
					}
					if (k + 1 < unstable[0].length) {
						swap[i][k + 1]++;
					}
				}
			}
		}
		return swap;
	}
}
