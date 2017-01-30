/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2015-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;


/**
 * Use a rate gyro to return the robots heading relative to a starting position. The Gyro class
 * tracks the robots heading based on the starting position. As the robot rotates the new heading is
 * computed by integrating the rate of rotation returned by the sensor. When the class is
 * instantiated, it does a short calibration routine where it samples the gyro while at rest to
 * determine the default offset. This is subtracted from each sample to determine the heading.
 *
 * <p>This class is for the digital ADXRS450 gyro sensor that connects via SPI.
 */
@SuppressWarnings({"TypeName", "AbbreviationAsWordInName", "PMD.UnusedPrivateField"})
public class ADXRS450_Gyro extends AnalogGyro  {
  private static final double kSamplePeriod = 0.001;
  private static final double kCalibrationSampleTime = 5.0;


  /**
   * Constructor.  Uses the onboard CS0.
   */
  public ADXRS450_Gyro() {
    super(0);
  }



  /**
   * Constructor.
   *
   * @param port The SPI port that the gyro is connected to
   */
//  public ADXRS450_Gyro(SPI.Port port) {
//    support.
//  }


 
}
