# 🐟 Fish Fillets NG – OOP Project 2025/2026

## 🎮 Introduction
This project consists of the implementation of a game engine inspired by the classic **Fish Fillets NG**. The goal is to control two fish (a small one and a big one) through various aquatic environments, solving puzzles and interacting with objects to reach the exit.

This project was developed in **Java** for the Object-Oriented Programming (POO) course, focusing on the application of design patterns, inheritance, polymorphism, and encapsulation.

---

## 🚀 How to Play

### Controls
The game is controlled via keyboard:

| Key | Action |
| :---: | --- |
| **Arrows / WASD** | Move the currently selected fish |
| **Space** | Switch between Small Fish and Big Fish |
| **R** | Restart the current level |

### Main Rules
1.  **Victory:** Both fish must leave the game grid to advance to the next level.
2.  **Defeat:** If any fish dies (crushed, eaten, or exploded), the level must be restarted.
3.  **Gravity:** Movable objects (except the fish) fall if they have no support, potentially causing damage.

---

## 🧩 Game Elements

### 🐠 Characters
* **Small Fish:**
    * Can pass through **Holed Walls**.
    * Can push only **1 light object**.
    * Dies if supporting more than one light object or any heavy object.
* **Big Fish:**
    * Can push multiple light objects or heavy objects.
    * **Cannot** pass through holed walls.
    * Dies if supporting more than one heavy object.

### 📦 Objects and Interactions

| Object | Type | Implemented Behavior |
| :--- | :--- | :--- |
| **Wall** | Fixed | Blocks movement. Supports any object. |
| **Steel (Pipe)** | Fixed | Blocks movement. Indestructible. |
| **Trunk (Wood)** | Fixed | Breaks and disappears if a heavy object falls on it. |
| **Cup** | Light | Simple object subject to gravity. |
| **Stone** | Heavy | Falls and can kill fish. **Extra:** Moving it might uncover a Krab. |
| **Anchor** | Heavy | Horizontal movement is limited to 1 position per push. |
| **Bomb** | Light | Explodes upon hitting the ground, destroying adjacent objects (except the fish carrying it). |
| **Trap** | Heavy | Kills the Big Fish on touch. The Small Fish can pass through it. |
| **Holed Wall** | Fixed | Allows the Small Fish to pass through. |

---

## ✨ Extra Features (Creativity)

Beyond the base requirements, advanced mechanics and unique objects were implemented:

### 1. 🧱 Juan (Our Creation)
A block with rudimentary "AI". When falling, if it encounters an obstacle, it attempts to **slide left or right** before stopping, making puzzles more dynamic and unpredictable.

### 2. 🎈 Buoy
An object with **inverted gravity**. Unlike other objects that fall, the buoy floats to the top of the screen if there are no obstacles. It can be used to block upper passages or lift other objects.

### 3. 🦀 Krab (Enemy)
An autonomous enemy that:
* Moves randomly around the scenario.
* Kills the **Small Fish** on touch.
* Is crushed/eaten by the **Big Fish**.
* *Spawn:* Can appear as a surprise when a Stone is moved.

### 4. 💥 Visual Feedback (Particles)
Implementation of temporary objects for visual feedback:
* **Explosion:** Appears when a bomb detonates, removing itself automatically after a few ticks.
* **Blood:** Appears when a fish or crab dies.

---

## 🛠️ Architecture and Design Patterns

The project follows a robust modular architecture based on SOLID principles.

### Class Hierarchy
* **GameObject:** Abstract base class. Contains position, room reference, and base rendering logic (`ImageTile`).
    * **MovableObject / NonMovableObject:** Primary behavior distinction.
        * **HeavyObject / LightObject:** Defines strength rules for the fish interactions.
    * **GameCharacter:** Base class for `SmallFish`, `BigFish`, and enemies, managing movement and death states.

### Key Interfaces
* **`Interactable`:** Allows each object to define its own logic when a fish tries to "enter" its cell (push, die, block, pass).
* **`Gravity`:** Physics abstraction.
    * Replacing the standard `fall()` method with `specialMov()` allowed the implementation of the **Buoy** (which goes up) and **Juan** (which slides sideways) using the same physics engine as the **Stone** (which goes down).

### Design Patterns Used
1.  **Singleton:** Used in `GameEngine`, `ImageGUI`, `SmallFish`, and `BigFish` to ensure unique instances and easy global access.
2.  **Observer:** The `GameEngine` observes the `ImageGUI` to react to keyboard inputs.
3.  **Factory Method (Simplified):** The file reader (`Room.java`) acts as a factory, instantiating the correct objects (`new Wall()`, `new Juan()`, etc.) based on the character read from the file.

---

## 🏆 Highscores
The game maintains a persistent record of the **Top 10** best scores.
* Criteria: **Lowest Time** (in ticks) and, in case of a tie, **Fewest Moves**.
* Data is stored persistently in `gamedata/scores.txt`.

---

## 👨‍💻 Authors
* **[Martim Barros](https://github.com/barrosw5)**
* **[Pedro Coelho](https://github.com/pecoelho01)** 
