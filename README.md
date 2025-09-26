# Pokémon Monster — PROG2 (SoSe 2025)

A small Java/JavaFX game for **Programming 2**: turn-based battles with type effectiveness, **Player vs AI (3 difficulty levels)**, and a simple GUI.

> **Tech:** Java 17, JavaFX 17 • **Run:** `./run.sh`

---

## Table of Contents
- [Features](#features)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Manual Compile & Run](#manual-compile--run)
- [Gameplay](#gameplay)
- [Troubleshooting](#troubleshooting)
- [Credits & License](#credits--license)

---

## Features
- Turn-based combat (HP, moves, switching).
- **Player vs AI** with **3 difficulty levels** (selectable in the game).
- Type effectiveness (incl. STAB) using `Typen-Multiplikator-Tabelle.xlsx`.
- JavaFX UI: main menu, login, selection screens, battle view, game over.
- Extensible Pokémon/moves data model.

---

## Project Structure

```text
PROG2SoSe25/
├─ Battle/                  # Battle engine (e.g., Battle.java)
├─ Players/
│  ├─ KI/                   # AI players/logic
│  └─ Users/                # Human player code
├─ Pokemon/
│  ├─ helper/               # Helpers
│  ├─ Pokemon.java          # Base class
│  ├─ Bisasam.java
│  ├─ Bisaflor.java
│  ├─ Evoli.java
│  ├─ Gengar.java
│  ├─ Glumanda.java
│  ├─ Glurak.java
│  ├─ Lektroball.java
│  ├─ Nachtara.java
│  ├─ Nebulak.java
│  ├─ Schiggy.java
│  └─ Turtok.java
├─ src/
│  ├─ app/
│  │  └─ MainMenu.java      # App entry / JavaFX launcher
│  └─ Scenes/               # JavaFX scenes (Login, Difficulty, Prep, Battle, GameOver)
│     ├─ BattlePrepScene.java
│     ├─ BattleView.java
│     ├─ DifficultyScene.java
│     ├─ GameOverView.java
│     └─ PokemonScene.java
├─ Images/                  # UI assets
├─ javafx-sdk-17.0.15/      # Bundled JavaFX SDK
├─ users/                   # (optional) local data
├─ build-jar/               # Build artifacts
├─ run.sh                   # Start script (recommended)
├─ Typen-Multiplikator-Tabelle.xlsx
└─ Anleitung.pdf            # Quick guide (German)
```
Requirements
JDK 17 — verify with java -version
JavaFX 17 — included at javafx-sdk-17.0.15/
macOS/Linux shell or Windows PowerShell
Quick Start
# macOS / Linux
```text
chmod +x run.sh
./run.sh
```
The script sets the JavaFX module path and launches the app.
Manual Compile & Run
Default entry class: app.MainMenu (change if your main differs).
macOS / Linux
# Compile
mkdir -p build-jar
javac --module-path javafx-sdk-17.0.15/lib --add-modules javafx.controls,javafx.fxml \
      -d build-jar $(find src -name "*.java")

# Run
java --module-path javafx-sdk-17.0.15/lib --add-modules javafx.controls,javafx.fxml \
     -cp build-jar app.MainMenu
Windows (PowerShell)
# Compile
mkdir build-jar
javac --module-path ".\javafx-sdk-17.0.15\lib" --add-modules javafx.controls,javafx.fxml `
      -d build-jar (Get-ChildItem -Recurse src -Filter *.java).FullName

# Run
```text
java --module-path ".\javafx-sdk-17.0.15\lib" --add-modules javafx.controls,javafx.fxml `
     -cp build-jar app.MainMenu
(Optional) Create a runnable JAR
jar --create --file build-jar/app.jar --main-class app.MainMenu -C build-jar .
java --module-path javafx-sdk-17.0.15/lib --add-modules javafx.controls,javafx.fxml -jar build-jar/app.jar
```
## Gameplay
```text
Login & Difficulty — choose Easy / Medium / Hard to set AI strength.
Pick Pokémon — choose your team; AI gets a team automatically.
Battle — HP bars at the top; move buttons bottom-left; Switch Pokémon bottom-right.
Win / Lose — reduce all enemy Pokémon to 0 HP to win.
Type Effectiveness: Multipliers from Typen-Multiplikator-Tabelle.xlsx are used in damage (STAB 1.5× included).
```
## Troubleshooting
```text
java.lang.module.FindException: Module javafx.controls not found
Use: --module-path <path>/javafx-sdk-17.0.15/lib --add-modules javafx.controls,javafx.fxml
Blank/black window
Ensure app.MainMenu launches a JavaFX Application via Application.launch(...).
Windows paths / classpath
Use backslashes and remember classpath separator differences (; on Windows).
```
## Credits & License
```text
Course: Programming 2 — SoSe 2025
Project: Pokémon Monster
Authors: Mohamed Atef Helali
License: Educational use for course assessment.
```

