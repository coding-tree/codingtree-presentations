# Sesja 3: Silnik Gry

> **Java na Luzie** -- kurs Javy dla tych, co chca kiedys zbudowac wlasnego Minecrafta.

Witaj ponownie, wojowniku! W sesji 1 stworzyliSmy bohaterow i arene walki. W sesji 2 dodalismy ekwipunek, crafting i enumy. Dzisiaj robimy cos naprawde poteznego -- **budujemy silnik gry**.

Tak, dobrze czytasz. Ten sam wzorzec, ktory dziala wewnatrz Minecrafta. Ten sam typ petli, ktora LWJGL (biblioteka graficzna Javy) uzywa do renderowania swiatow 3D. Roznica? Minecraft ma miliony linii kodu. My zaczniemy od kilkudziesieciu -- ale **architektura** bedzie identyczna.

Gotowy? Odpalamy silnik!

---

## Interfejsy -- kontrakty miedzy obiektami

Do tej pory uzywalismy **dziedziczenia** (`extends`) -- "Dragon jest Monsterem, ktory jest Entity". To swietne, ale ma limit: Java pozwala dziedziczyc tylko po **jednej** klasie.

A co, jesli chcesz powiedziec: "Entity umie sie renderowac I aktualizowac I obslugiwac kolizje"? Potrzebujesz czegos innego -- **interfejsow**.

### Analogia: port USB

Pomysl o porcie USB w komputerze. Nie obchodzi go, czy podlaczasz myszkE, klawiature, pendrive'a czy gamepad. Jedyne co go interesuje, to czy urzadzenie **spelnIA standard USB**.

Interfejs w Javie to dokladnie taki standard. Mowi: "jesli chcesz byc Renderable, musisz miec metode `render()`". Nie mowi JAK renderowac -- to juz twoja sprawa. Smok moze wyswietlac ASCII art, a gracz -- statystyki. Oba sa Renderable.

```
     Interfejs Renderable         Interfejs Updatable
     "umiem sie rysowac"          "umiem sie aktualizowac"
           |                              |
     void render()                void update(deltaTime)
           |                              |
     +-----+-----+               +--------+--------+
     |     |     |               |        |        |
   Player Monster Dragon       Player  Monster  Dragon
```

### Python vs Java

W Pythonie interfejsy nie istnieja formalnie -- uzywasz **duck typing** ("jesli chodzi jak kaczka i kwacze jak kaczka..."). W Javie musisz jawnie powiedziec: "implementuje ten interfejs". Kompilator sprawdzi, czy naprawde masz wszystkie wymagane metody.

```python
# Python -- duck typing (zero gwarancji)
class Gracz:
    def render(self):
        print("gracz")

# Java -- jawny kontrakt (kompilator pilnuje)
class Player implements Renderable {
    @Override
    public void render() {
        System.out.println("gracz");
    }
}
```

---

## Krok 1: Interfejs Renderable -- "umiem sie rysowac"

Interfejs to kontrakt -- lista metod, ktore klasa MUSI zaimplementowac, jesli chce go uzywac.

Stworz plik `engine/Renderable.java`:

```java
package pl.codingtree.javanluzie.engine;

public interface Renderable {
    void render();
}
```

### Co tu sie dzieje?

- **`interface`** zamiast `class` -- to nie jest klasa, to kontrakt.
- **`void render()`** -- metoda bez ciala (bez klamr `{}`). Interfejs mowi CO robic, ale nie JAK.
- Kazda klasa, ktora napisze `implements Renderable`, MUSI miec metode `render()`. Inaczej -- blad kompilacji.

To jest **abstrakcja** -- ukrywamy szczegoly implementacji za prostym kontraktem.

---

## Krok 2: Interfejs Updatable -- "umiem sie aktualizowac"

Drugi kontrakt -- dla obiektow, ktore zmieniaja swoj stan w czasie.

Stworz plik `engine/Updatable.java`:

```java
package pl.codingtree.javanluzie.engine;

public interface Updatable {
    void update(double deltaTime);
}
```

### deltaTime -- co to?

`deltaTime` to czas, ktory uplynal od ostatniego ticku (w sekundach). Dlaczego to wazne?

Wyobraz sobie, ze postac porusza sie o 100 pikseli na sekunde. Bez deltaTime:
- Szybki komputer: 60 FPS -> postac przesunie sie 60 razy -> za szybko!
- Wolny komputer: 20 FPS -> postac przesunie sie 20 razy -> za wolno!

Z deltaTime:
- Szybki: `100 * 0.016s = 1.6px` na klatke, 60 razy = 96px/s
- Wolny: `100 * 0.05s = 5px` na klatke, 20 razy = 100px/s

**deltaTime wyrownuje predkosc niezaleznie od FPS!** Minecraft, Unity, Unreal -- wszystkie silniki to robia.

---

## Krok 3: Entity implementuje oba interfejsy

Czas na cos fajnego -- jedna klasa moze implementowac **wiele interfejsow naraz**! W Javie nie mozesz dziedziczyc po dwoch klasach, ale mozesz implementowac ile chcesz interfejsow.

Zmodyfikuj `entity/Entity.java` -- dodaj `implements Renderable, Updatable`:

```java
package pl.codingtree.javanluzie.entity;

import pl.codingtree.javanluzie.engine.Renderable;
import pl.codingtree.javanluzie.engine.Updatable;

public class Entity implements Renderable, Updatable {
    private String name;
    private int health;
    private int maxHealth;
    private int attackPower;

    public Entity(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attackPower = attackPower;
    }

    // ... istniejace metody (attack, takeDamage, isAlive, gettery, toString) ...

    @Override
    public void render() {
        System.out.println("  [" + name + "] HP: " + health + "/" + maxHealth + " ATK: " + attackPower);
    }

    @Override
    public void update(double deltaTime) {
        // Base entities don't do anything on update — subclasses can override
    }
}
```

### Co sie zmienilo?

- **`implements Renderable, Updatable`** -- Entity teraz spelnia OBA kontrakty. Przecinek oddziela interfejsy.
- **`render()`** -- wyswietla status entity. Kazda podklasa (Player, Dragon) moze to nadpisac na swoj sposob.
- **`update(double deltaTime)`** -- bazowa wersja nic nie robi. Podklasy moga nadpisac -- np. Monster moglby sie poruszac, Dragon moglby regenerowac HP.

### Wiele interfejsow vs dziedziczenie

```
       Entity
       extends   <- mozesz dziedziczyc tylko po JEDNEJ klasie
       /    \
   Player  Monster
             |
           Dragon

       Entity
       implements  <- mozesz implementowac WIELE interfejsow!
       /    \
  Renderable  Updatable
```

To potezna cecha Javy -- klasa moze miec **jednego rodzica**, ale **wiele kontraktow**. To jak czlowiek: mozesz byc jednoczesnie kierowca, programista i gracz -- kazda "rola" to osobny interfejs.

---

## Krok 4: GameConfig -- record (niemutowalny obiekt)

Silnik gry potrzebuje konfiguracji -- rozmiar swiata, maksymalna liczba entity, tryb debug. Zamiast zwyklej klasy z polami i getterami, uzyj **recordu** -- nowosci w Javie, ktora robi to automatycznie.

Stworz plik `engine/GameConfig.java`:

```java
package pl.codingtree.javanluzie.engine;

public record GameConfig(
    int worldWidth,
    int worldHeight,
    int maxEntities,
    boolean debugMode,
    String title
) {
    public static GameConfig defaultConfig() {
        return new GameConfig(800, 600, 100, false, "Java na Luzie");
    }
}
```

### Co to record?

Record to klasa, ktora automatycznie generuje:
- **Konstruktor** -- `new GameConfig(800, 600, 100, false, "Java na Luzie")`
- **Gettery** -- `config.worldWidth()`, `config.title()` (bez `get` na poczatku!)
- **`toString()`** -- ladne wyswietlanie
- **`equals()` i `hashCode()`** -- porownanie po wartosciach

To odpowiednik Pythonowego `dataclass` z `frozen=True`:

```python
# Python
@dataclass(frozen=True)
class GameConfig:
    world_width: int = 800
    world_height: int = 600
```

### Dlaczego record, a nie zwykla klasa?

Bo konfiguracja **nie powinna sie zmieniac** w trakcie gry. Record jest **niemutowalny** -- raz stworzony, nie mozesz zmienic zadnego pola. Zero setterow. To gwarancja, ze nikt przypadkiem nie zmieni rozmiaru swiata w polowie gry.

### Slowo kluczowe `final`

ZauwazyliScie `final` w wczesniejszych sesjach przy polach. W recordach pola sa automatycznie `final`. Ale warto znac to slowo:

```java
final int maxHP = 100;        // nie mozna zmienic wartosci
final List<Entity> entities;  // nie mozna przypisac nowej listy,
                               // ALE mozna dodawac/usuwac elementy!
```

**`final` chroni referencje, nie zawartosc!** To jak przyklad z zycia: masz **staly** adres domu (`final`), ale mozesz wnosic i wynosic meble (modyfikowac zawartosc listy).

---

## Krok 5: GameWorld -- scena pelna obiektow

GameWorld to **scene graph** -- kontener, ktory trzyma wszystkie entity w grze i zarzadza nimi. Sam tez jest Updatable i Renderable -- bo aktualizacja swiata to aktualizacja wszystkich entity w nim.

Stworz plik `engine/GameWorld.java`:

```java
package pl.codingtree.javanluzie.engine;

import pl.codingtree.javanluzie.entity.Entity;
import java.util.ArrayList;
import java.util.List;

public class GameWorld implements Updatable, Renderable {
    private final List<Entity> entities;
    private final GameConfig config;

    public GameWorld(GameConfig config) {
        this.entities = new ArrayList<>();
        this.config = config;
    }

    public void addEntity(Entity entity) {
        if (entities.size() < config.maxEntities()) {
            entities.add(entity);
            System.out.println("[World] Dodano: " + entity.getName());
        } else {
            System.out.println("[World] Swiat pelny! Max: " + config.maxEntities());
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        System.out.println("[World] Usunieto: " + entity.getName());
    }

    public List<Entity> getEntities() {
        return List.copyOf(entities);
    }

    public void removeDeadEntities() {
        entities.removeIf(e -> !e.isAlive());
    }

    @Override
    public void update(double deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
        removeDeadEntities();
    }

    @Override
    public void render() {
        if (config.debugMode()) {
            System.out.println("[Debug] Entities: " + entities.size());
        }
        for (Entity entity : entities) {
            entity.render();
        }
    }
}
```

### Co tu sie dzieje?

- **`implements Updatable, Renderable`** -- GameWorld SAM jest Updatable i Renderable. To jak pudElko w pudelku -- mozesz miec swiat w swiecie.
- **`private final List<Entity> entities`** -- lista wszystkich entity. `final` = nie mozna przypisac nowej listy, ale mozna dodawac/usuwac entity.
- **`config.maxEntities()`** -- uzywamy gettera z recordu (bez `get`!).
- **`entities.removeIf(e -> !e.isAlive())`** -- **lambda!** To skrocona forma: "usun kazdy element `e`, dla ktorego `e.isAlive()` zwraca `false`". W Pythonie to byloby `lista = [e for e in lista if e.is_alive()]`.

### Dlaczego mozemy wywolac render() i update() bezposrednio?

Bo Entity implementuje **oba** interfejsy -- Renderable i Updatable. Kompilator wie, ze kazdy Entity ma te metody, wiec nie musimy sprawdzac `instanceof`. Gdybysmy mieli w liscie rozne typy (nie tylko Entity), musielibysmy sprawdzac -- ale tu mamy pewnosc.

Pamietasz `instanceof` z sesji 1 (Arena)? Tam sprawdzalismy, czy winner jest Playerem. To przydaje sie, gdy nie znasz dokladnego typu obiektu.

---

## Krok 6: GameLoop -- serce silnika

Oto moment, na ktory czekales. **Game Loop** to wzorzec architektoniczny, ktory napedza KAZDA gre -- od Ponga po Cyberpunk 2077.

Kazda klatka gry sklada sie z dwoch krokow:
1. **Update** -- zaktualizuj stan swiata (rusz postacie, sprawdz kolizje, policz obrazenia)
2. **Render** -- narysuj wszystko na ekranie

I tak w kolko, 60 razy na sekunde (albo wiecej).

Stworz plik `engine/GameLoop.java`:

```java
package pl.codingtree.javanluzie.engine;

public class GameLoop {
    private final GameWorld world;
    private final GameConfig config;
    private boolean running;
    private int maxTicks;

    public GameLoop(GameWorld world, GameConfig config, int maxTicks) {
        this.world = world;
        this.config = config;
        this.maxTicks = maxTicks;
    }

    public void start() {
        running = true;
        System.out.println("\n=== " + config.title() + " — START ===\n");

        int tick = 0;
        long lastTime = System.nanoTime();

        while (running && tick < maxTicks) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            tick++;
            System.out.println("--- Tick " + tick + " (dt: " + String.format("%.4f", deltaTime) + "s) ---");

            world.update(deltaTime);
            world.render();

            if (world.getEntities().isEmpty()) {
                System.out.println("Swiat jest pusty! Koniec gry.");
                running = false;
            }

            System.out.println();
        }

        System.out.println("=== GAME OVER ===");
    }

    public void stop() {
        running = false;
    }
}
```

### Petla gry -- krok po kroku

```
start()
  |
  v
[WHILE running]
  |
  +-> Oblicz deltaTime (czas od ostatniego ticku)
  |
  +-> world.update(deltaTime)  <- aktualizuj stan
  |       |
  |       +-> Dla kazdego Entity: entity.update(deltaTime)
  |       +-> Usun martwe entity
  |
  +-> world.render()           <- narysuj wszystko
  |       |
  |       +-> Dla kazdego Entity: entity.render()
  |
  +-> Sprawdz czy swiat nie jest pusty
  |
  [KONIEC PETLI]
  |
  v
GAME OVER
```

### System.nanoTime() -- pomiar czasu

`System.nanoTime()` zwraca czas w **nanosekundach** (miliardowe czesci sekundy). Dzielimy przez `1_000_000_000.0` zeby dostac sekundy. Podloga `_` w liczbach to czytelnosc -- Java je ignoruje. `1_000_000_000` to to samo co `1000000000`, ale latwiejsze do przeczytania.

### To jest DOKLADNIE to co robi Minecraft!

Minecraft uzywa biblioteki **LWJGL** (Lightweight Java Game Library) do renderowania grafiki 3D. Ale pod spodem? Ta sama petla:

```java
// Uproszczony Minecraft (pseudo-kod)
while (running) {
    double dt = calculateDeltaTime();

    handleInput();        // Klawiatura, myszka
    world.update(dt);     // Fizyka, AI, crafting
    world.render();       // OpenGL renderowanie

    glfwSwapBuffers();    // Pokaz klatke na ekranie
    glfwPollEvents();     // Sprawdz eventy systemowe
}
```

Roznica miedzy naszym silnikiem a Minecraftem? Minecraft uzywa `glfwSwapBuffers()` do wyswietlania grafiki 3D przez OpenGL, a my uzywamy `System.out.println()`. Ale **architektura jest identyczna**. Gdybys podlaczyl LWJGL do naszego silnika, moglbys rysowac prawdziwa grafike zamiast tekstu!

---

## Krok 7: InputHandler -- obsluga klawiatury

W prawdziwej grze gracz naciska klawisze. Potrzebujemy interfejsu do obslugi inputu.

Stworz plik `engine/InputHandler.java`:

```java
package pl.codingtree.javanluzie.engine;

public interface InputHandler {
    void onKey(String key);
}
```

### Jak to dziala w praktyce?

```java
// Player moglby implementowac InputHandler:
public class Player extends Entity implements InputHandler {
    @Override
    public void onKey(String key) {
        switch (key) {
            case "W" -> System.out.println(getName() + " idzie do przodu!");
            case "A" -> System.out.println(getName() + " idzie w lewo!");
            case "S" -> System.out.println(getName() + " idzie do tylu!");
            case "D" -> System.out.println(getName() + " idzie w prawo!");
            case "SPACE" -> System.out.println(getName() + " skacze!");
        }
    }
}
```

Zwroc uwage na **switch z `->` (strzalka)** -- to nowsza skladnia Javy, czystsza niz tradycyjne `case: break;`. Kazdy przypadek wykonuje jedno wyrazenie.

W LWJGL obsluga klawiszy wyglada podobnie, ale zamiast stringow uzywasz stalych typu `GLFW_KEY_W`, a `onKey` jest wywolywany przez system okienkowy.

---

## Krok 8: Main -- odpalamy silnik!

Zaktualizuj `Main.java`:

```java
package pl.codingtree.javanluzie;

import pl.codingtree.javanluzie.engine.GameConfig;
import pl.codingtree.javanluzie.engine.GameLoop;
import pl.codingtree.javanluzie.engine.GameWorld;
import pl.codingtree.javanluzie.entity.Dragon;
import pl.codingtree.javanluzie.entity.Monster;
import pl.codingtree.javanluzie.entity.Player;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Java na Luzie - Sesja 3: Silnik Gry ===\n");

        GameConfig config = new GameConfig(800, 600, 50, true, "Arena RPG");
        GameWorld world = new GameWorld(config);

        Player hero = new Player("Rycerz", 100, 15);
        Monster goblin = new Monster("Goblin", 30, 5, "Maly Noz");
        Dragon dragon = new Dragon("Smaug", 80, 10);

        world.addEntity(hero);
        world.addEntity(goblin);
        world.addEntity(dragon);

        GameLoop loop = new GameLoop(world, config, 5);
        loop.start();
    }
}
```

### Co sie tutaj dzieje?

1. **Tworzymy konfiguracje** -- swiat 800x600, max 50 entity, debug wlaczony.
2. **Tworzymy swiat** -- pusty kontener na entity.
3. **Dodajemy bohaterow** -- rycerz, goblin, smok.
4. **Tworzymy petlE gry** -- max 5 tickow (zeby nie lecial w nieskonczonosc).
5. **Start!** -- silnik odpalony, petla kreci sie 5 razy.

### Uruchom program!

```bash
mvn compile exec:java -Dexec.mainClass="pl.codingtree.javanluzie.Main"
```

Zobaczysz ticki silnika, debug info z liczba entity, i renderowanie kazdej postaci. To twoj pierwszy silnik gry!

---

## Podsumowanie -- czego sie dzis nauczyles

| Koncept | Co to robi | Python equivalent |
|---|---|---|
| **Interface** | Kontrakt -- lista metod do zaimplementowania | ABC / duck typing |
| **`implements`** | Klasa spelnia kontrakt interfejsu | `class Foo(ABC)` |
| **Wiele interfejsow** | Klasa implementuje kilka kontraktow naraz | Wielodziedziczenie |
| **`record`** | Niemutowalna klasa z auto-getterami | `@dataclass(frozen=True)` |
| **`final`** | Referencja nie do zmiany | Brak (konwencja) |
| **Game Loop** | Petla update/render -- serce kazdej gry | `while True: update(); draw()` |
| **deltaTime** | Czas miedzy klatkami -- wyrownuje predkosc | Tak samo |
| **`default` method** | Metoda z implementacja w interfejsie | Zwykla metoda w ABC |
| **Lambda** | Skrocona funkcja anonimowa | `lambda x: ...` |
| **Pattern matching** | `instanceof` + rzutowanie w jednym kroku | `isinstance()` |

### Architektura naszego silnika:

```
GameLoop
  |
  +-> GameWorld (implements Updatable, Renderable)
        |
        +-> List<Entity>
              |
              +-> Player (implements Renderable, Updatable)
              +-> Monster (implements Renderable, Updatable)
              +-> Dragon (implements Renderable, Updatable)

Interfejsy:
  Renderable  -- render()
  Updatable   -- update(deltaTime)
  InputHandler -- onKey(key)
  Collidable  -- getX/Y/Width/Height, collidesWith()

GameConfig (record) -- niemutowalna konfiguracja
```

---

## BOSS FIGHT CHALLENGE: Interfejs Collidable

Czas na wyzwanie! W kazdej grze obiekty ze soba koliduja -- gracz wchodzi na potwoRA, pocisk trafia w sciane. Stworzymy interfejs **Collidable** z **default method** -- metoda, ktora ma implementacje WEWNATRZ interfejsu!

### Krok 1: Stworz interfejs

Stworz plik `engine/Collidable.java`:

```java
package pl.codingtree.javanluzie.engine;

public interface Collidable {
    double getX();
    double getY();
    double getWidth();
    double getHeight();

    default boolean collidesWith(Collidable other) {
        return getX() < other.getX() + other.getWidth() &&
               getX() + getWidth() > other.getX() &&
               getY() < other.getY() + other.getHeight() &&
               getY() + getHeight() > other.getY();
    }
}
```

### Co to `default`?

`default` pozwala dac interfejsowi **implementacje metody**. Normalne metody w interfejsie nie maja ciala -- `default` jest wyjatkiem.

Dlaczego to przydatne? Bo `collidesWith()` dziala TAK SAMO dla kazdego obiektu -- sprawdza, czy dwa prostokaty na siebie nachodza (AABB collision detection). Nie ma sensu pisac tej logiki w kazdej klasie osobno.

### AABB -- Axis-Aligned Bounding Box

To algorytm kolizji uzywany w **kazdej** grze 2D (i wielu 3D). Sprawdza, czy dwa prostokaty sie przecinaja:

```
  +------+
  | A    |
  |   +--+---+
  +---+--+   |
      |    B |
      +------+

A koliduje z B, bo:
  A.x < B.x + B.width  ORAZ
  A.x + A.width > B.x  ORAZ
  A.y < B.y + B.height ORAZ
  A.y + A.height > B.y
```

Minecraft uzywa dokladnie tego algorytmu do sprawdzania, czy gracz dotyka bloku!

### Krok 2: Zaimplementuj Collidable w Entity

Dodaj pola `x`, `y`, `width`, `height` do Entity i zaimplementuj Collidable:

```java
public class Entity implements Renderable, Updatable, Collidable {
    // ... istniejace pola ...
    private double x, y;
    private double width = 1.0, height = 1.0;

    // ... istniejace metody ...

    @Override
    public double getX() { return x; }
    @Override
    public double getY() { return y; }
    @Override
    public double getWidth() { return width; }
    @Override
    public double getHeight() { return height; }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
```

### Krok 3: Sprawdz kolizje w GameWorld

Dodaj metode do GameWorld:

```java
public void checkCollisions() {
    List<Entity> list = new ArrayList<>(entities);
    for (int i = 0; i < list.size(); i++) {
        for (int j = i + 1; j < list.size(); j++) {
            Entity a = list.get(i);
            Entity b = list.get(j);
            if (a instanceof Collidable ca && b instanceof Collidable cb) {
                if (ca.collidesWith(cb)) {
                    System.out.println("KOLIZJA: " + a.getName() + " <-> " + b.getName());
                }
            }
        }
    }
}
```

### Krok 4: Przetestuj!

W `Main.java` ustaw pozycje entity i sprawdz kolizje:

```java
hero.setPosition(0, 0);
goblin.setPosition(0.5, 0.5);  // Nachodzi na hero!
dragon.setPosition(10, 10);     // Daleko

world.checkCollisions();
// Powinno wypisac: KOLIZJA: Rycerz <-> Goblin
```

### Bonus:

Zmodyfikuj `update()` w GameWorld, zeby automatycznie sprawdzal kolizje co tick. Gdy entity koliduja -- niech sie atakuja!

Powodzenia, inzynierze! W nastepnej (ostatniej!) sesji odkryjemy **supermoc Javy** -- Optionale, Streamy, sealed classes i przygotujemy sie do prawdziwego LWJGL!
