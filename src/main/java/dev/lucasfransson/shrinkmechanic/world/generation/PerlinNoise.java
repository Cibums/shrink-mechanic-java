package dev.lucasfransson.shrinkmechanic.world.generation;
import java.util.Random;

public class PerlinNoise {

	private int[] permutation;

	public PerlinNoise() {
		Random rnd = new Random();
		long randomSeed = rnd.nextLong();
		initPerlin(randomSeed);
	}

	public PerlinNoise(long seed) {
		initPerlin(seed);
	}

	private void initPerlin(long seed) {
		Random rnd = new Random(seed);
		permutation = new int[512];
		int[] p = new int[256];

		for (int i = 0; i < 256; i++)
			p[i] = i;

		// Shuffle using seed
		for (int i = 255; i > 0; i--) {
			int j = rnd.nextInt(i + 1);
			int temp = p[i];
			p[i] = p[j];
			p[j] = temp;
		}

		for (int i = 0; i < 512; i++)
			permutation[i] = p[i & 255];
	}

	private double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	private double lerp(double a, double b, double t) {
		return a + t * (b - a);
	}
	private double grad(int hash, double x, double y) {
		return ((hash & 1) == 0 ? x : -x) + ((hash & 2) == 0 ? y : -y);
	}

	public double perlin(double x, double y) {
		int xi = (int) Math.floor(x) & 255;
		int yi = (int) Math.floor(y) & 255;
		double xf = x - Math.floor(x);
		double yf = y - Math.floor(y);

		double u = fade(xf);
		double v = fade(yf);

		int aa = (permutation[xi] + yi) & 255;
		int ab = (permutation[xi] + yi + 1) & 255;
		int ba = (permutation[xi + 1] + yi) & 255;
		int bb = (permutation[xi + 1] + yi + 1) & 255;

		return lerp(
				lerp(grad(permutation[aa], xf, yf),
						grad(permutation[ba], xf - 1, yf), u),
				lerp(grad(permutation[ab], xf, yf - 1),
						grad(permutation[bb], xf - 1, yf - 1), u),
				v);
	}
}
