package com.shinemo.mpush.common.container;

import java.util.List;

public class Car implements Lifecycle{

	private Light leftLight; //左大灯
	private Light rightLight; //右大灯
	
	private Engine engine;
	
	private BaseLifecycle lifecycle = new BaseLifecycle(this);
	
	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		lifecycle.addLifecycleListener(listener);
	}

	@Override
	public List<LifecycleListener> getLifecycleListeners() {
		return lifecycle.getListeners();
	}

	@Override
	public void start() {
		
		lifecycle.fireLifecycleEvent(LifecyclePhase.BEFORE_START, null);
		//先初始化其他组件
		if(engine!=null&&engine instanceof Lifecycle){
			engine.start();
		}
		if(leftLight!=null&&leftLight instanceof Lifecycle){
			leftLight.start();
		}
		if(rightLight!=null&&rightLight instanceof Lifecycle){
			rightLight.start();
		}
		System.out.println("car start ...");
		lifecycle.fireLifecycleEvent(LifecyclePhase.AFTER_START, null);
	
	}

	@Override
	public void stop() {
		
		//先关闭其他组件
		if(engine!=null&&engine instanceof Lifecycle){
			engine.stop();
		}
		if(leftLight!=null&&leftLight instanceof Lifecycle){
			leftLight.stop();
		}
		if(rightLight!=null&&rightLight instanceof Lifecycle){
			rightLight.stop();
		}
		lifecycle.fireLifecycleEvent(LifecyclePhase.BEFORE_STOP, null);
		System.out.println("car stop ...");
		lifecycle.fireLifecycleEvent(LifecyclePhase.AFTER_STOP, null);
		
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
		engine.addLifecycleListener(new LifecycleListener() {
			
			@Override
			public void lifecycleEvent(LifecycleEvent event) {
				if(Lifecycle.LifecyclePhase.AFTER_START.equals(event.getPhase())){
					System.out.println("监听到发动机启动了，轰轰轰。。。");
				}else{
					System.out.println("engine:"+event.getPhase());
				}
			}
		});
		
		car.addLifecycleListener(new LifecycleListener() {
			
			@Override
			public void lifecycleEvent(LifecycleEvent event) {
				if(Lifecycle.LifecyclePhase.AFTER_STOP.equals(event.getPhase())){
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
