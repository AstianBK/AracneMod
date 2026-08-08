package com.astianbk.aracnemod.common.worldgenerator.density;

import com.astianbk.aracnemod.common.worldgenerator.WorldNoise;
import net.minecraft.world.phys.Vec3;

public class IslandSource implements DensitySource {

    public final double centerX;
    public final double centerY;
    public final double centerZ;

    public final double radius;

    private final WorldNoise noise;


    public IslandSource(double centerX, double centerY, double centerZ, double radius, WorldNoise noise) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.radius = radius;
        this.noise = noise;
    }


    @Override
    public double sample(double x, double y, double z) {

        double dx = x - centerX;
        double dz = z - centerZ;


        double distance = Math.sqrt(dx * dx + dz * dz);

        double shapeNoise = noise.terrain(x, y, z);


        double deformation = shapeNoise * 15.0;


        double finalRadius = radius + deformation;


        return finalRadius - distance;

    }
    public Vec3 getCenter(){
        return new Vec3(centerX,centerY,centerZ);
    }
}
