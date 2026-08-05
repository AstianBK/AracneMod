package com.astianbk.aracnemod.common.worldgenerator;

public class WorldNoise {

    private final FastNoiseLite terrainNoise;
    private final FastNoiseLite detailNoise;
    private final FastNoiseLite bridgeNoise;

    public WorldNoise(long seed) {

        terrainNoise = new FastNoiseLite((int) seed);
        terrainNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        terrainNoise.SetFrequency(0.008f);
        terrainNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        terrainNoise.SetFractalOctaves(4);
        terrainNoise.SetFractalLacunarity(2.0f);
        terrainNoise.SetFractalGain(0.5f);

        detailNoise = new FastNoiseLite((int) seed + 1);
        detailNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        detailNoise.SetFrequency(0.03f);
        detailNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        detailNoise.SetFractalOctaves(3);

        bridgeNoise = new FastNoiseLite((int) seed + 2);
        bridgeNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        bridgeNoise.SetFrequency(0.05f);
    }

    public double terrain(double x, double y, double z) {
        return terrainNoise.GetNoise((float)x, (float)y, (float)z);
    }

    public double detail(double x, double y, double z) {
        return detailNoise.GetNoise((float)x, (float)y, (float)z);
    }

    public double bridge(double x, double y, double z) {
        return bridgeNoise.GetNoise((float)x, (float)y, (float)z);
    }
}