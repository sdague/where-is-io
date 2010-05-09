/*
** main.c
** 
** Made by (Sean Dague)
** Login   <sdague@orac>
** 
** Started on  Fri Apr 16 10:48:16 2010 Sean Dague
** Last update Sun May 12 01:17:25 2002 Speed Blue
*/
#include <stdio.h>
#include <time.h>
#include <math.h>
#include "l1.h"

double julian_date(time_t t)
{
    return ((t / 86400.0) + 2440587.5);
}

double dotprod(double A[], double B[])
{
    double prod = A[0] * B[0] + A[1] * B[1] + A[2] * B[2];
    return prod;
}

void crossprod(double A[], double B[], double C[])
{
    C[0] = B[1] * A[2] - B[2] * A[1];
    C[1] = B[2] * A[0] - B[0] * A[2];
    C[2] = B[0] * A[1] - B[1] * A[0];
}

double mag(double xyz[])
{
    return sqrt(xyz[0]*xyz[0] + xyz[1]*xyz[1] + xyz[2]*xyz[2]);
}

void unitv(double A[], double I[])
{
    double m = mag(A);
    I[0] = A[0] / sqrt(m);
    I[1] = A[1] / sqrt(m);
    I[2] = A[2] / sqrt(m);
}

double compute_delta(double jd, double moon[])
{
    double earth[3];
    get_earth_helio_coordsv (jd, earth);

    double jupiter[3];
    get_jupiter_helio_coordsv(jd, jupiter);

    double moonl[3];
    double moond[3];
    double Avector[3];
    double Cvector[3];
    int j = 0;
    for (j = 0; j < 3; j++) {
        moond[j] = jupiter[j] + moon[j];
        Avector[j] = moond[j] - earth[j];
        Cvector[j] = jupiter[j] - earth[j];
    }

    // double theta = acos(dotprod(Avector, Cvector) / (mag(Avector) * mag(Cvector)));
    /* double Cross[3]; */
    /* Cross[0] = Cvector[1] * Avector[2] - Cvector[2] * Avector[1]; */
    /* Cross[1] = Cvector[2] * Avector[0] - Cvector[0] * Avector[2]; */
    /* Cross[2] = Cvector[0] * Avector[1] - Cvector[1] * Avector[0]; */

    // double y = mag(Avector) * sin(theta);
    
    // printf("Z: %10.10f\n", Cvector[0] * Avector[1] - Cvector[1] * Avector[0]);

    double moonv[3];
    crossprod(Cvector, Avector, moonv);

    double planev[3] = {0.0, 0.0, 1.0};
    // crossprod(earth, jupiter, planev);
    
    double inplanev[3];
    crossprod(planev, Avector, inplanev);

    double inplanevunit[3];
    unitv(inplanev, inplanevunit);

    double y = dotprod(inplanevunit, moon);

    double z = moonv[2];
    moonv[2] = 0; // this makes us work in a plane
    
    //    double y = mag(moonv);
    
    /* double A = mag(Avector); // sqrt(pow(earth[0] - moond[0], 2) + pow(earth[1] - moond[1], 2) + pow(earth[2] - moond[2], 2)); */
    /* double B = mag(moon); // sqrt(pow(moon[0], 2) + pow(moon[1], 2) + pow(moon[2], 2));  */
    /* double C = mag(Cvector); // sqrt(pow(earth[0] - jupiter[0], 2) + pow(earth[1] - jupiter[1], 2) + pow(earth[2] - jupiter[2], 2));  */
    
    /* double y = sqrtl(powl(A,2) - (powl( ( powl(C,2) + powl(A,2) - powl(B,2) ) / (2*C) , 2 ) ) );  */

    /* double z = Cvector[0] * Avector[1] - Cvector[1] * Avector[0]; */

    if (z < 0) {
        // y = -1.0 * y;
    }
    
    return (double)y;
}

void main() 
{
    time_t t = time(NULL);
    double jd0 = julian_date(1272672000);
    // 1271474022);
    
    // jd -= 10.0;
    double i;

    for (i = 0.0; i < 300; i += 0.01) { 
        double jd = jd0 + i;
        
        double io[3];
        get_io_parent_coordsv(jd, io);

        double europa[3];
        get_europa_parent_coordsv(jd, europa);
        
        double ganymede[3];
        get_ganymede_parent_coordsv(jd, ganymede);

        double calisto[3];
        get_callisto_parent_coordsv(jd, calisto);

        double ioy = compute_delta(jd, io);
        double europay = compute_delta(jd, europa);
        double ganymedey = compute_delta(jd, ganymede);
        double calistoy = compute_delta(jd, calisto);

        /* printf("%10.10f %10.10f %10.10f\n", earth[0], earth[1], earth[2]); */
        /* printf("%10.10f %10.10f %10.10f\n", jupiter[0], jupiter[1], jupiter[2]); */
        /* printf("%10.10f %10.10f %10.10f\n", calisto[0], calisto[1], calisto[2]); */
        
        /* printf("A = %10.10f, B = %10.10f, C = %10.10f, y = %10.10f\n", A, B, C, y); */

        printf("%10.10f %10.10f %10.10f %10.10f %10.10f \n", jd, ioy, europay, ganymedey, calistoy);
    }
    //    printf("pow(A,2) = %10.10f, B = %10.10f, C = %10.10f, y = %10.10f\n", pow(A,2), (pow(C,2) + pow(A,2) - pow(B,2)) / C, ((pow(C,2) + pow(A,2) - pow(B,2))/(2*C)), y);

    /* for (i = 0.0; i < 30; i += 0.01) { */
    /*     double j = jd + i; */
    /*     // printf("JD = %f\n", jd); */
    /*     double xyz1[3]; */
    /*     double xyz2[3]; */
    /*     double xyz3[3]; */
    /*     double xyz4[3]; */
    /*     GetL1Coor(j, 0, xyz1); */
    /*     GetL1Coor(j, 1, xyz2); */
    /*     GetL1Coor(j, 2, xyz3); */
    /*     GetL1Coor(j, 3, xyz4); */
    /*     // printf("x: %10.10f, y: %10.10f, z: %10.10f\n", xyz[0], */
    /*     // xyz[1], xyz[2]); */
    /*     // printf("%10.10f %10.10f %10.10f %10.10f %10.10f\n", j, -1.0 * xyz1[1], -1.0 * xyz2[1], -1.0 * xyz3[1], -1.0 * xyz4[1]); */
    /*     printf("%10.10f %10.10f %10.10f %10.10f %10.10f\n", j, -1.0 * xyz1[0], -1.0 * xyz1[1], -1.0 * xyz4[0], -1.0 * xyz4[1]); */
    /* } */

}
