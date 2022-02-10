package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLED;


public class LEDSubsystem extends SubsystemBase {
  /**
   * Creates a new ExampleSubsystem.
   */
  private AddressableLED m_LED;
  private AddressableLEDBuffer m_LEDBuffer;
  private int m_rainbowFirstPixelHue;
  private int m_blinkCounter;
  public enum LEDState
	{
		kRED, kBLUE, k1014COLOR, kRAINBOW, kLOW_BATTERY, kAMERICA, kWHITE, kOFF;
	}

  public LEDSubsystem(AddressableLED LED, AddressableLEDBuffer LEDBuffer) {
    m_LED = LED;
    m_LEDBuffer = LEDBuffer;
    
    m_LED.setLength(m_LEDBuffer.getLength());

    // Set the data
    m_LED.setData(m_LEDBuffer);
    m_LED.start();
  }

  @Override
  public void periodic() {
    // This is the loop to do things...

    // else {
    //   setLightsPattern(LEDState.kRAINBOW);
    // }
  }

  public void setLightsRGB(int red, int green, int blue) {
    for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
      // Sets the specified LED to the RGB values
      m_LEDBuffer.setRGB(i, red, green, blue);
   }
   
   m_LED.setData(m_LEDBuffer);
  }

  public void setLightsHSV(int hue, int saturation, int value) {
    for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
      // Sets the specified LED to the HSV values
      m_LEDBuffer.setRGB(i, hue, saturation, value);
   }
   
   m_LED.setData(m_LEDBuffer);
  }

  public void setLightsRainbow() { //should be put inside loop
    // For every pixel
    for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_LEDBuffer.getLength())) % 180;
      // Set the value
      m_LEDBuffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
    m_LED.setData(m_LEDBuffer);
  }

  public void setLightsBlink(int red, int green, int blue, int blinkSpeed) { //must be put inside loop
    m_blinkCounter++;
    if (m_blinkCounter < blinkSpeed && m_blinkCounter >= 0) 
      setLightsRGB(red, green, blue);
    else if (m_blinkCounter < blinkSpeed*2) 
      setLightsPattern(LEDState.kOFF);
    else 
      m_blinkCounter = 0;
    
  }

  public void setLightsAmerica() {
    for (var i = 0; i < m_LEDBuffer.getLength()/3; i++) {
      // Sets the specified LED to the RGB values
      m_LEDBuffer.setRGB(i, 255, 0, 0);
    }
    for (var i = m_LEDBuffer.getLength()/3; i < m_LEDBuffer.getLength()/3*2; i++) {
      m_LEDBuffer.setRGB(i, 255, 255, 255);
    }
    for (var i = m_LEDBuffer.getLength()/3*2; i < m_LEDBuffer.getLength(); i++) {
      m_LEDBuffer.setRGB(i, 0, 0, 255);
    }
    m_LED.setData(m_LEDBuffer);
  }

  public void setLightsPattern(LEDState state) {
    switch (state) {
      case kRED:
        setLightsRGB(255, 0, 0);
        break;
      case kBLUE:
        setLightsRGB(0, 0, 255);
        break;
      case k1014COLOR:
        setLightsRGB(235, 255, 0);
        break;
      case kOFF:
        setLightsRGB(0, 0, 0);
        break;
      case kLOW_BATTERY:
        setLightsBlink(255, 0, 0, 10);
        break;
      case kRAINBOW:
        setLightsRainbow();
        break;
      case kAMERICA:
        setLightsAmerica();
        break;
      case kWHITE:
        setLightsRGB(255, 255, 255);
    }
  }
}