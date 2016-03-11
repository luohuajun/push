package com.shinemo.mpush.common.container;

import java.util.List;

public class Car implements LifeCycle{

	private Light leftLight; //左大灯
	private Light rightLight; //右大灯
	
	private Engine engine;
	
	private BaseLifeCycle lifecycle = new BaseLifeCycle(this);
	
	@Override
	public void addLifeCycleListener(LifeCycleListener listener) {
		lifecycle.addLifeCycleListener(listener);
	}

	@Override
	public List<LifeCycleListener> getLifeCycleListeners() {
		return lifecycle.getListeners();
	}

	@Override
	public void start() {
		
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.BEFORE_START, null);
		//先初始化其他组件
		if(engine!=null&&engine instanceof LifeCycle){
			engine.start();
		}
		if(leftLight!=null&&leftLight instanceof LifeCycle){
			leftLight.start();
		}
		if(rightLight!=null&&rightLight instanceof LifeCycle){
			rightLight.start();
		}
		System.out.println("car start ...");
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.AFTER_START, null);
	
	}

	@Override
	public void stop() {
		
		//先关闭其他组件
		if(engine!=null&&engine instanceof LifeCycle){
			engine.stop();
		}
		if(leftLight!=null&&leftLight instanceof LifeCycle){
			leftLight.stop();
		}
		if(rightLight!=null&&rightLight instanceof LifeCycle){
			rightLight.stop();
		}
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.BEFORE_STOP, null);
		System.out.println("car stop ...");
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.AFTER_STOP, null);
		
	}

	public Light getLeftLight() {
		return leftLight;
	}

	public void setLeftLight(Light leftLight) {
		this.leftLight = leftLight;
	}

	public Light getRightLight() {
		return rightLight;
	}

	public void setRightLight(Light rightLight) {
		this.rightLight = rightLight;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	public static void main(String[] args) throws InterruptedException {
		Light leftLight = new Light("左灯");
		Light rightLight = new Light("右灯");
		Engine engine = new Engine();
		Car car = new Car();
		car.setLeftLight(leftLight);
		car.setRightLight(rightLight);
		car.setEngine(engine);
		engine.addLifeCycleListener(new LifeCycleListener() {
			
			@Override
			public void lifeCycleEvent(LifeCycleEvent event) {
				if(LifeCycle.LifeCyclePhase.AFTER_START.equals(event.getPhase())){
					System.out.println("监听到发动机启动了，轰轰轰。。。");
				}else{
					System.out.println("engine:"+event.getPhase());
				}
			}
		});
		
		car.addLifeCycleListener(new LifeCycleListener() {
			
			@Override
			public void lifeCycleEvent(LifeCycleEvent event) {
				if(LifeCycle.LifeCyclePhase.AFTER_STOP.equals(event.getPhase())){
					System.out.println("car 快停止了，下车吧。。。");
				}else{
					System.out.println("car:"+event.getPhase());
				}
			}
			
		});
		
		car.start();
		
		Thread.sleep(3000);
		
		car.stop();
	}
	
}
