package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.util.AllocationException;


public abstract class InterruptableSensorBase
{
	public boolean interruptEnabled = false;
	public boolean interruptSynchronous = false;
	
	public boolean triggerRisingEdge = false;
	public boolean triggerFallingEdge = false;
	
	private boolean lastInterruptEdgeFalling;
	private boolean lastInterruptEdgeRising;
	
	private long lastInterruptTimeRising = 0;
	private long lastInterruptTimeFalling = 0;
	
	//null iff interruptSynchronous is true
	public InterruptHandlerFunction<?> handler;
	public Object parameter; 
	
	  public enum WaitResult {
	    kTimeout(0x0), kRisingEdge(0x1), kFallingEdge(0x100), kBoth(0x101);

	    public final int value;

	    private WaitResult(int value) {
	      this.value = value;
	    }
	  }
	
  /**
   * Request one of the 8 interrupts asynchronously on this digital input.
   *
   * @param handler The {@link InterruptHandlerFunction} that contains the method {@link
   *                InterruptHandlerFunction#interruptFired(int, Object)} that will be called
   *                whenever there is an interrupt on this device. Request interrupts in synchronous
   *                mode where the user program interrupt handler will be called when an interrupt
   *                occurs. The default is interrupt on rising edges only.
   */
  public void requestInterrupts(InterruptHandlerFunction<?> handler)
  {
    if(interruptEnabled) {
      throw new AllocationException("The interrupt has already been allocated");
    }
    
	interruptEnabled = true;
	interruptSynchronous = false;

    setUpSourceEdge(true, false);
    this.handler = handler;
    this.parameter = handler.overridableParameter();
  }

  /**
   * Request one of the 8 interrupts synchronously on this digital input. Request interrupts in
   * synchronous mode where the user program will have to explicitly wait for the interrupt to occur
   * using {@link #waitForInterrupt}. The default is interrupt on rising edges only.
   */
  public void requestInterrupts() {
    if(interruptEnabled) {
      throw new AllocationException("The interrupt has already been allocated");
    }

	interruptEnabled = true;
	interruptSynchronous = true;
	
	this.handler = null;
	this.parameter = null;
	
    setUpSourceEdge(true, false);

  }
  
  /**
   * Cancel interrupts on this device. This deallocates all the chipobject structures and disables
   * any interrupts.
   */
  public void cancelInterrupts() {
    if (!interruptEnabled) {
      throw new IllegalStateException("The interrupt is not allocated.");
    }
    
    interruptEnabled = false;
  }

  /**
   * In synchronous mode, wait for the defined interrupt to occur.
   *
   * @param timeout        Timeout in seconds
   * @param ignorePrevious If true, ignore interrupts that happened before waitForInterrupt was
   *                       called.
   * @return Result of the wait.
   */
  public WaitResult waitForInterrupt(double timeout, boolean ignorePrevious) {
    if (!(interruptEnabled && interruptSynchronous)) {
      throw new IllegalStateException("The interrupt is not allocated.");
    }
    
    //TODO: ignorePrevious = false is not implemented
    
    long startTime;
    WaitResult result = null;
    
    try
    {
    	
	    synchronized(this)
	    {
	    	startTime = System.nanoTime();
	    	this.wait((long)(timeout * 1000));
	    	
	    	if(startTime + timeout * 1e6 > System.nanoTime())
	    	{
	    		result = WaitResult.kTimeout;
	    	}
	    	if(lastInterruptEdgeFalling && lastInterruptEdgeRising)
	    	{
	   			result = WaitResult.kBoth;
	    	}
	    	else if(lastInterruptEdgeFalling)
	    	{
	    		result = WaitResult.kFallingEdge;
	    	}
	    	else if(lastInterruptEdgeRising)
	    	{
	    		result = WaitResult.kRisingEdge;
	    	}
	    }
    }
	catch(InterruptedException e)
    {
		return null;
    }
    
    return result;
  }

  /**
   * In synchronous mode, wait for the defined interrupt to occur.
   *
   * @param timeout Timeout in seconds
   * @return Result of the wait.
   */
  public WaitResult waitForInterrupt(double timeout) {
    return waitForInterrupt(timeout, true);
  }

  /**
   * Enable interrupts to occur on this input. Interrupts are disabled when the RequestInterrupt
   * call is made. This gives time to do the setup of the other options before starting to field
   * interrupts.
   */
  public void enableInterrupts() {
    if (interruptEnabled) {
      throw new IllegalStateException("The interrupt is not allocated.");
    }
    if (interruptSynchronous) {
      throw new IllegalStateException("You do not need to enable synchronous interrupts");
    }
    
    //TODO: not implemented
  }

  /**
   * Disable Interrupts without without deallocating structures.
   */
  public void disableInterrupts() {
	if (!interruptEnabled) {
	  throw new IllegalStateException("The interrupt is not allocated.");
	}
	if (interruptSynchronous) {
	  throw new IllegalStateException("You do not need to enable synchronous interrupts");
	}
	
	//TODO: not implemented
  }

  /**
   * Return the timestamp for the rising interrupt that occurred most recently. This is in the same
   * time domain as getClock(). The rising-edge interrupt should be enabled with {@link
   * #setUpSourceEdge}.
   *
   * @return Timestamp in seconds since boot.
   */
  public double readRisingTimestamp() {
    if (!interruptEnabled) {
      throw new IllegalStateException("The interrupt is not allocated.");
    }
    return lastInterruptTimeRising;
  }

  /**
   * Return the timestamp for the falling interrupt that occurred most recently. This is in the same
   * time domain as getClock(). The falling-edge interrupt should be enabled with {@link
   * #setUpSourceEdge}.
   *
   * @return Timestamp in seconds since boot.
   */
  public double readFallingTimestamp() {
    if (!interruptEnabled) {
      throw new IllegalStateException("The interrupt is not allocated.");
    }
    return lastInterruptTimeRising;
  }

  /**
   * Set which edge to trigger interrupts on.
   *
   * @param risingEdge  true to interrupt on rising edge
   * @param fallingEdge true to interrupt on falling edge
   */
  public void setUpSourceEdge(boolean risingEdge, boolean fallingEdge) {
    if(interruptEnabled) {
      triggerRisingEdge = risingEdge;
      triggerFallingEdge = fallingEdge;
    } else {
      throw new IllegalArgumentException("You must call RequestInterrupts before setUpSourceEdge");
    }
  }
  
  /**
   * Called from subclasses when the value of the input change
   * @param newState
   */
  @SuppressWarnings("unchecked")
protected void onStateChange(boolean newState)
  {
	  if(interruptEnabled)
	  {
		  //calculate bitmask for interrupt handler function
		  int interruptMask;
		  
		  if(newState)
		  {
			  interruptMask = 0b10;
		  }
		  else
		  {
			  interruptMask = 0b1;
		  }
		  lastInterruptEdgeRising = newState;
		  lastInterruptEdgeFalling = !newState;
				  
		  if(newState && triggerRisingEdge)
		  {
			  lastInterruptTimeRising = System.currentTimeMillis();
			  if(!interruptSynchronous)
			  {
				  ((InterruptHandlerFunction<Object>)handler).interruptFired(interruptMask, parameter);
			  }
		  }
		  else
		  {
			  lastInterruptTimeFalling = System.currentTimeMillis();
			  if(!interruptSynchronous)
			  {
				  ((InterruptHandlerFunction<Object>)handler).interruptFired(interruptMask, parameter);
			  }
		  }
		  
		  // notify waiting threads with a synchronous interrupt
		  if(interruptSynchronous)
		  {
			synchronized(this)
			{
				this.notifyAll();
			}
		  }
	  }
  }
}
