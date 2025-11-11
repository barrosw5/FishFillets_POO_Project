# Object-Oriented Programming Project – 2025/2026 [v1.0]

## 🎮 Introduction

This project consists of creating a game inspired by **Fish Fillets NG**.  
The main goal is to develop the **game engine** that allows two user-controlled characters — the **small fish** and the **big fish** — to navigate aquatic environments, interact with objects, and reach the exit in each level.  

- Game type: Classic arcade
- Characters: Small fish and big fish
- Interactions: Movement, pushing objects, carrying objects, and avoiding hazards
- Failure condition: Death of any character restarts the level

> Original game videos: [YouTube Playlist](https://www.youtube.com/playlist?list=PL0YH3AsYQfx1julFNzXls1gd9ZRLPinv3)  
> More info: [Fish Fillets NG - Wikipedia](https://en.wikipedia.org/wiki/Fish_Fillets_NG)  

---

## 🏆 Project Objectives

- Develop the **game engine** using the GUI provided by the instructors.
- Allow the two fish to exit multiple aquatic environments while interacting with objects.
- Ensure the engine is **flexible**, enabling new objects or behaviors to be added at any time.
- Implement a **persistent highscore table** based on completion time and number of moves.

---

## 📋 Movement Rules

### Small Fish
- Can carry **only one light object** at a time
- Can push **one light object** horizontally or vertically
- Can pass through **walls with holes**
- Objects being carried will **sink** if the fish moves horizontally

### Big Fish
- Can carry **multiple light objects or one heavy object**
- Can push:
  - Horizontally: multiple light or heavy objects
  - Vertically: only one light or heavy object
- **Cannot pass through walls with holes**
- Objects being carried will **sink** when moving horizontally

---

## 🧩 Object Behavior

### Movable Objects
| Object   | Weight | Movement / Effect |
|----------|--------|------------------|
| Cup      | Light  | Can move in all 4 directions |
| Stone    | Heavy  | Can move in all 4 directions |
| Anchor   | Heavy  | Can move horizontally, 1 position |
| Bomb     | Light  | Explodes when sinking, removes adjacent objects, may kill fish |
| Trap     | Heavy  | Kills big fish; small fish can pass through |

### Fixed Objects
| Object           | Effect |
|-----------------|--------|
| Trunk            | Removed if a heavy object falls on it |
| Vertical/Horizontal Steel Pipe   | Can support any object |
| Wall             | Can support any object |
| Holed Wall   | Can support any object but can be passed by small fish or bowls |

---

## 🕹️ Gameplay

- Players must move **both fish** to the exit in each level.
- Objects can have **positive or negative effects**:
  - Positive: Allow access to the exit
  - Negative: Kill a fish (e.g., falling objects)
- Objects can be **single-use** (disappear after interaction) or **multi-use** (remain in the level).
- Movement counters are updated in real-time in the **top information bar**.

---

## 🏅 Highscores

- A **persistent highscore table** displays the **10 best scores**.
- Scoring is based on:
  - Completion time
  - Number of moves per fish
- Scores are stored on the system file to maintain them between sessions.

---

## 📂 Resources

- Images and assets used in the game come from [Fish Fillets NG](https://fillets.sourceforge.net/download.php) and are included in the `images/` folder.
