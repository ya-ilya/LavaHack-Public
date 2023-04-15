package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;

public class RenderHandEvent extends Event {
    private final float ticks;

    public RenderHandEvent(float ticks) {this.ticks = ticks;}
    public float getPartialTicks() {return ticks;}
    public static class PostOutline extends RenderHandEvent { public PostOutline(float ticks) {super(ticks);}}
    public static class PreOutline extends RenderHandEvent { public PreOutline(float ticks) {super(ticks);}}
    public static class PostFill extends RenderHandEvent { public PostFill(float ticks) {super(ticks);}}
    public static class PreFill extends RenderHandEvent { public PreFill(float ticks) {super(ticks);}}
}
