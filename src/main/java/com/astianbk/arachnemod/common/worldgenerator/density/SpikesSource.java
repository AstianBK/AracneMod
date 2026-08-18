package com.astianbk.arachnemod.common.worldgenerator.density;

import com.astianbk.arachnemod.common.worldgenerator.WorldNoise;
import net.minecraft.world.phys.Vec3;

public class SpikesSource implements DensitySource{
    public final double centerX;
    public final double centerY;
    public final double centerZ;

    public final double radius;
    public final double height;

    private final WorldNoise noise;

    public SpikesSource(double centerX, double centerY, double centerZ, double radius, double height, WorldNoise noise) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.radius = radius;
        this.height = height;
        this.noise = noise;
    }

    @Override
    public double sample(double x, double y, double z) {
        double dx = x - centerX;
        double dz = z - centerZ;

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        double relativeY = (y - centerY) / height;

        if (relativeY < 0.0 || relativeY > 1.0) {
            return -1.0;
        }

        double currentRadius = radius * (1.0 - relativeY);

        double shapeNoise = noise.terrain(x, y, z);
        currentRadius += shapeNoise * 5.0;

        return currentRadius - horizontalDistance;
    }

    public Vec3 getCenter() {
        return new Vec3(centerX, centerY, centerZ);
    }
}
