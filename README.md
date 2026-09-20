# SprintLock

![Powered by OneConfig](https://polyfrost.org/media/branding/badges/badge_1.svg)

A lightweight Minecraft 1.8.9 Forge mod by **liwwyy** that keeps the sprint key locked on.

### Features
- OneConfig toggle: **Enable Sprint Lock**
- Sprint-key lock based on the same key-state approach used by PolySprint
- Does not force `EntityPlayerSP#setSprinting`
- Does not send custom sprint packets
- Designed to coexist with other client-side sprint/toggle mods
- Polyfrost Gradle Toolkit 0.6.5

When enabled, releasing or pressing the normal sprint key cannot clear the locked key state. Minecraft's normal movement code remains responsible for sprinting and networking.
