package com.astianbk.aracnemod.common.worldgenerator.density;

import com.astianbk.aracnemod.common.worldgenerator.WorldNoise;
import net.minecraft.world.phys.Vec3;

public class BridgeSource implements DensitySource{
    public final Vec3 from;
    public final Vec3 to;

    public final double radius;

    private final WorldNoise noise;


    public BridgeSource(Vec3 from, Vec3 to, double radius, WorldNoise noise) {
        this.from = from;
        this.to = to;
        this.radius = radius;
        this.noise = noise;
    }


    @Override
    public double sample(double x,double y,double z) {
        double ax = from.x;
        double ay = from.y;
        double az = from.z;


        double bx = to.x;
        double by = to.y;
        double bz = to.z;


        double abx = bx-ax;
        double aby = by-ay;
        double abz = bz-az;


        double apx=x-ax;
        double apy=y-ay;
        double apz=z-az;


        double length = abx*abx+ aby*aby+ abz*abz;


        double t = (apx*abx+ apy*aby+ apz*abz) / length;


        t=Math.max(0,Math.min(1,t));


        double cx=ax+abx*t;
        double cy=ay+aby*t;
        double cz=az+abz*t;


        double dx=x-cx;
        double dy=y-cy;
        double dz=z-cz;


        double distance = Math.sqrt(dx*dx+ dy*dy+ dz*dz);


        double bridgeNoise = noise.bridge(x, y, z);


        double noisyRadius = radius + bridgeNoise * 2.0;


        return noisyRadius-distance;
    }
}
