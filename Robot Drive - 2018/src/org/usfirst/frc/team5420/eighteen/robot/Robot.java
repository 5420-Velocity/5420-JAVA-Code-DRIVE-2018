
package org.usfirst.frc.team5420.eighteen.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5420.eighteen.robot.commands.ExampleCommand;
import org.usfirst.frc.team5420.eighteen.robot.subsystems.ExampleSubsystem;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;


public class Robot extends IterativeRobot {
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI jinterface;
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
	
	// Setup of the Devices in the Code.
	Timer timer = new Timer();
	protected ADXRS450_Gyro gyroSensor;
	
	public static Compressor compressor0;
	// Claw Control
	public static  Solenoid solenoid0, solenoid1;
	// Clutch Control
	public static  Solenoid solenoid3,  solenoid4;
	
	public static Encoder encoder0, encoder1;
	public static DigitalInput UpperLimit, LowerLimit;
	
	public static Joystick joystick0, joystick1;
	public static VictorSP LiftMotor;
	
	// Motor Setup
	protected VictorSP motorFL, motorBL, motorBR, motorFR;
	
	/**
	 * The RobotInit code to start the setup.
	 */
	@Override
	public void robotInit() {
		SmartDashboard.putBoolean("AutoInitComplete", false);
		SmartDashboard.putBoolean("TeleopInitComplete", false);
		
		jinterface = new OI();
		
		gyroSensor = new ADXRS450_Gyro( SPI.Port.kOnboardCS0 );
		gyroSensor.calibrate();
		
		solenoid0 = new Solenoid(1); // Claw Close
		solenoid1 = new Solenoid(2); // Claw Open
		solenoid3 = new Solenoid(3); // Clutch Engage
		solenoid4 = new Solenoid(4); // Clutch DisEngage
		
		encoder0 = new Encoder(4,5, false, Encoder.EncodingType.k4X); // Left
		encoder1 = new Encoder(6,7, false, Encoder.EncodingType.k4X); // Right
		compressor0 = new Compressor(0);
		
		joystick0 = new Joystick(0); //Controller One
		joystick1 = new Joystick(1); //Controller Two
		
		UpperLimit = new DigitalInput(0);
		LowerLimit = new DigitalInput(9);
		
		LiftMotor= new VictorSP(2);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		
		heartbeat();
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		SmartDashboard.putBoolean("AutoInitComplete", true);
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
		SmartDashboard.putNumber("RunningHeartbeat", Timer.getMatchTime());
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		
		SmartDashboard.putBoolean("TeleopInitComplete", true);
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		heartbeat();
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	/**
	 * This Smartdashboard 'RunningHeartbeat' is updating the Runtime value of the Robot.
	 * Used in the dashboard to see if the robot is running. Send the Unix Time of the RIO.
	 * @link https://stackoverflow.com/a/732043/5779200
	 */
	public void heartbeat(){
		SmartDashboard.putNumber("RunningHeartbeat", System.currentTimeMillis() / 1000L);
	}
}
