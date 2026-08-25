package com.astianbk.arachnemod.common.worldgenerator.the_void.density;

import com.astianbk.arachnemod.common.worldgenerator.the_void.WorldNoise;

public class TunnelSource implements com.astianbk.arachnemod.common.worldgenerator.density.DensitySource {

    private final IslandSource island;

    private final int radialCount;
    private final double tunnelRadius;

    private final double waveAmplitude;
    private final double waveFrequency;
    private final WorldNoise noise;
    private double radialAmplitude;
    private double verticalFrequency;
    private double radialFrequency;
    private double verticalAmplitude;

    public TunnelSource(IslandSource island, int radialCount, double tunnelRadius, double waveAmplitude, double waveFrequency, WorldNoise noise) {

        this.island = island;
        this.radialCount = radialCount;
        this.tunnelRadius = tunnelRadius;
        this.waveAmplitude = waveAmplitude;
        this.waveFrequency = waveFrequency;
        this.noise = noise;
        radialAmplitude = 0.1F;
        verticalAmplitude = 0.02;
        verticalFrequency = 0.4;
        radialFrequency = 0.6;
    }

    @Override
    public double sample(double x, double y, double z) {

        double dx = x - island.centerX;
        double dz = z - island.centerZ;

        double horizontalDistanceSquared =
                dx * dx + dz * dz;

        double maxRadius =
                island.radius + tunnelRadius;

        if (horizontalDistanceSquared >
                maxRadius * maxRadius) {

            return -1;
        }

        double horizontalDistance =
                Math.sqrt(horizontalDistanceSquared);

        /*
         * Coordenada vertical relativa.
         */
        double relativeY =
                y - 125;

        double best =
                -Double.MAX_VALUE;

        for (int i = 0; i < radialCount; i++) {

            double angle =
                    i * Math.PI * 2.0 / radialCount;

            double cos =
                    Math.cos(angle);

            double sin =
                    Math.sin(angle);

            double radial =
                    dx * cos +
                            dz * sin;

            double lateral =
                    -dx * sin +
                            dz * cos;

            /*
             * Curvatura horizontal.
             */
            double horizontalWave =
                    Math.sin(
                            radial *
                                    radialFrequency
                    ) * radialAmplitude;

            lateral -= horizontalWave;

            double verticalWave =
                    Math.sin(
                            radial *
                                    verticalFrequency
                    ) * verticalAmplitude;

            double tunnelY =
                    verticalWave;

            double verticalDistance =
                    relativeY - tunnelY;

            double distance =
                    Math.sqrt(
                            lateral * lateral +
                                    verticalDistance *
                                            verticalDistance
                    );

            best = Math.max(
                    best,
                    tunnelRadius - distance
            );
        }

        double centralRadius =
                5.0;

        double central =
                centralRadius -
                        horizontalDistance;


        double centerWave =
                Math.sin(
                        relativeY *
                                0.08
                ) * 3.0;

        double centralX =
                dx - centerWave;

        double centralDistance =
                Math.sqrt(
                        centralX * centralX +
                                dz * dz
                );

        central =
                centralRadius -
                        centralDistance;

        best = Math.max(
                best,
                central
        );


        double[] ringLevels = {
                -60,
                -30,
                0,
                30
        };

        for (double ringY : ringLevels) {
            double ringRadius = island.radius * 0.55;
            double deformation = Math.sin(Math.atan2(dz, dx) * 4.0) * 3.0;

            double desiredRadius = ringRadius + deformation;

            double radialDistance = Math.abs(horizontalDistance - desiredRadius);

            double verticalDistance = Math.abs(relativeY - ringY) + deformation;

            double distance = Math.sqrt(radialDistance * radialDistance + verticalDistance * verticalDistance);

            best = Math.max(best, tunnelRadius - distance);
        }

        return best;
    }
}