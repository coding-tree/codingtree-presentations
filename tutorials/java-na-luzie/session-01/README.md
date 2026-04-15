# Sesja 1: Stworz Swoj Swiat

> **Java na Luzie** -- kurs Javy dla tych, co chca kiedys zbudowac wlasnego Minecrafta.

Witaj, wojowniku! Dzisiaj zaczynamy przygode z Java -- jezykiem, w ktorym powstal Minecraft, serwery bankowe i miliony aplikacji na Androida. Znasz juz Pythona? Swietnie. Java to jak Python, ale w zbroi -- szybszy, bardziej zdyscyplinowany i gotowy do powaZnych bitew.

W tej sesji stworzymy system walki jak w grze RPG. Bedziemy mieli bohaterow, potwory, smoki i arene, na ktorej beda walczyc na smierc i zycie. Gotowy? Lecimy!

---

## Python vs Java -- szybkie porownanie

Zanim wskoczysz do Javy, zobacz czym sie rozni od Pythona, ktorego juz znasz:

| Cecha | Python | Java |
|---|---|---|
| Typy zmiennych | Dynamiczne (`x = 5`) | Statyczne (`int x = 5;`) |
| Kompilacja | Interpretowany | Kompilowany do bytecodu (JVM) |
| "Ja" w metodach | `self` | `this` |
| Bloki kodu | Wciecia (tabulatory) | Klamry `{ }` |
| Sredniki | Nie ma | Obowiazkowe `;` |
| Szybkosc | Wolniejszy | Znacznie szybszy |
| Klasy | Opcjonalne | Wszystko jest w klasie! |

**Przyklad -- ta sama rzecz w dwoch jezykach:**

Python:
```python
class Wojownik:
    def __init__(self, name, hp):
        self.name = name
        self.hp = hp

    def przedstaw_sie(self):
        print(f"{self.name} ma {self.hp} HP")
```

Java:
```java
public class Wojownik {
    private String name;
    private int hp;

    public Wojownik(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    public void przedstawSie() {
        System.out.println(name + " ma " + hp + " HP");
    }
}
```

Widzisz roznice? W Javie musisz powiedziec kompilatorowi **dokladnie** jaki typ ma kazda zmienna. Na poczatku moze sie wydawac upierdliwe, ale uwierz -- to twoj najlepszy przyjaciel. Kompilator lapie bledy **zanim** uruchomisz program, a nie w trakcie dzialania jak w Pythonie.

---

## Dlaczego Java jest swietna?

- **Statyczne typy = mniej bugow.** Pomyliles typ? Kompilator ci powie od razu, a nie w polowie gry.
- **JVM = predkosc.** Java kompiluje sie do bytecodu, ktory JVM optymalizuje w locie. Minecraft na Pythonie? Mialby 2 FPS.
- **Ogromny ekosystem.** Maven, Spring, LibGDX, LWJGL -- miliony bibliotek gotowych do uzycia.
- **Minecraft jest w Javie!** Notch napisal go w Javie. Chcesz budowac gry? Jestes we wlasciwym miejscu.

---

## Klasy -- fundamenty twojego swiata

W Javie **wszystko** jest obiektem. Chcesz stworzyc postac w grze? Potrzebujesz klasy. Klasa to taki blueprint -- przepis na obiekt.

Kazda klasa ma trzy glowne elementy:
1. **Pola (fields)** -- dane obiektu (imie, HP, sila ataku)
2. **Konstruktor** -- metoda tworzaca obiekt (jak `__init__` w Pythonie)
3. **Metody** -- co obiekt umie robic (atakuj, przyjmij obrazenia, sprawdz czy zyje)

---

## Krok 1: Klasa Entity -- baza wszystkiego

Kazda postac w grze -- gracz, potwor, smok -- to jakis byt (Entity). Zamiast pisac te same pola i metody sto razy, stworzymy jedna klase bazowa.

Stworz plik `entity/Entity.java`:

```java
package pl.codingtree.javanluzie.entity;

public class Entity {
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

    public void attack(Entity target) {
        System.out.println(name + " atakuje " + target.getName() + "!");
        target.takeDamage(attackPower);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
        System.out.println(name + " otrzymuje " + damage + " obrazen! HP: " + health + "/" + maxHealth);
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Getters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttackPower() { return attackPower; }

    @Override
    public String toString() {
        return name + " [HP: " + health + "/" + maxHealth + ", ATK: " + attackPower + "]";
    }
}
```

### Co tu sie dzieje?

- **`private`** -- pola sa ukryte przed swiatem zewnetrznym. Nikt nie moze bezposrednio zmienic HP bohatera (zeby oszukiwac!). To sie nazywa **enkapsulacja**.
- **Konstruktor** `Entity(String name, int health, int attackPower)` -- tworzy nowy byt. `this.name = name` znaczy "moje pole `name` ustaw na wartosc parametru `name`".
- **`attack(Entity target)`** -- atakuje inny byt. Zwroc uwage, ze parametr to `Entity` -- mozesz atakowac **kazdego** kto jest Entity!
- **`takeDamage(int damage)`** -- przyjmuje obrazenia. HP nie moze spasc ponizej 0.
- **`isAlive()`** -- zwraca `true` jesli HP > 0. W Pythonie to bylby `def is_alive(self): return self.hp > 0`.
- **Gettery** -- publiczne metody do odczytu prywatnych pol. `getName()` zamiast bezposredniego dostepu do `name`.

### Co to `@Override` i `toString()`?

`toString()` to specjalna metoda, ktora Java wywoluje gdy chcesz wyswietlic obiekt. Bez niej `System.out.println(entity)` wypisze cos w stylu `Entity@1a2b3c` -- adres w pamieci, kompletnie bezuzyteczny.

`@Override` to **adnotacja** -- informuje kompilator: "hej, celowo nadpisuje metode z klasy nadrzednej". Jesli zrobisz literowke w nazwie metody, kompilator cierypie blad. Bez `@Override` po prostu stworzyBys nowa metode i zastanawialbys sie godzine, dlaczego nie dziala.

---

## Krok 2: Player -- gracz wchodzi do gry

Gracz to Entity, ale ma cos wiecej -- poziom i doswiadczenie. W Javie uzywamy **dziedziczenia** (`extends`), zeby powiedziec: "Player jest specjalnym rodzajem Entity".

Stworz plik `entity/Player.java`:

```java
package pl.codingtree.javanluzie.entity;

public class Player extends Entity {
    private int level;
    private int experience;

    public Player(String name, int health, int attackPower) {
        super(name, health, attackPower);
        this.level = 1;
        this.experience = 0;
    }

    public void gainExperience(int xp) {
        experience += xp;
        System.out.println(getName() + " zdobywa " + xp + " XP! (Lacznie: " + experience + ")");
        if (experience >= level * 100) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience = 0;
        System.out.println("*** " + getName() + " awansuje na poziom " + level + "! ***");
    }

    public int getLevel() { return level; }

    @Override
    public String toString() {
        return super.toString() + " LVL: " + level;
    }
}
```

### Kluczowe slowa:

- **`extends Entity`** -- Player dziedziczy WSZYSTKO po Entity (pola, metody, konstruktor dostepny przez `super`).
- **`super(name, health, attackPower)`** -- wywoluje konstruktor klasy nadrzednej (Entity). To jak `super().__init__()` w Pythonie.
- **`super.toString()`** -- w `toString()` wywolujemy najpierw wersje z Entity, a potem dodajemy level. Nie kopiujemy kodu -- **rozszerzamy** go!
- **`private void levelUp()`** -- metoda prywatna, tylko Player moze ja wywolac. Swiat zewnetrzny nie powinien recznie zmieniac levelu.

---

## Krok 3: Monster -- potwory na horyzoncie

Potwory tez sa Entity, ale maja loot (przedmioty do zdobycia) i czasem pudluja ataki.

Stworz plik `entity/Monster.java`:

```java
package pl.codingtree.javanluzie.entity;

import java.util.Random;

public class Monster extends Entity {
    private String lootDrop;
    private static final Random random = new Random();

    public Monster(String name, int health, int attackPower, String lootDrop) {
        super(name, health, attackPower);
        this.lootDrop = lootDrop;
    }

    public String dropLoot() {
        System.out.println(getName() + " upuszcza: " + lootDrop + "!");
        return lootDrop;
    }

    @Override
    public void attack(Entity target) {
        if (random.nextInt(100) < 20) {
            System.out.println(getName() + " pudluje!");
            return;
        }
        super.attack(target);
    }

    public String getLootDrop() { return lootDrop; }
}
```

### Cos nowego!

- **`import java.util.Random`** -- importujemy klase Random z biblioteki standardowej. W Pythonie to bylby `import random`.
- **`static final Random random = new Random()`** -- jedno `Random` dla WSZYSTKICH potworow. `static` = wspolne dla klasy, nie dla obiektu. `final` = nie mozna zmienic.
- **`@Override public void attack(...)`** -- **nadpisujemy** metode `attack()` z Entity! To jest **polimorfizm** -- ta sama metoda zachowuje sie inaczej w roznych klasach. Monster ma 20% szans na pudlo.
- **`super.attack(target)`** -- jesli nie spudlowal, wywolujemy oryginalna metode ataku z Entity.

---

## Krok 4: Dragon -- boss level!

Smok to Monster, ale na sterydach. Zieje ogniem i zadaje podwojne obrazenia. To jest moment, w ktorym polimorfizm naprawde blyska.

Stworz plik `entity/Dragon.java`:

```java
package pl.codingtree.javanluzie.entity;

public class Dragon extends Monster {

    public Dragon(String name, int health, int attackPower) {
        super(name, health, attackPower, "Smocza Luska");
    }

    @Override
    public void attack(Entity target) {
        System.out.println("🔥 " + getName() + " zieje ogniem na " + target.getName() + "! 🔥");
        target.takeDamage(getAttackPower() * 2);
    }

    @Override
    public String toString() {
        return "🐉 " + super.toString();
    }
}
```

### Zwroc uwage:

- **Dragon extends Monster** -- smok to potwor, ktory jest bytem. Lancuch dziedziczenia: `Dragon -> Monster -> Entity`.
- **Konstruktor** -- smok zawsze upuszcza "Smocza Luska", wiec hardkodujemy loot w konstruktorze.
- **`attack()`** -- smok NIE pudluje (nie wywoluje `super.attack()` z Monstera!). Zamiast tego zieje ogniem i zadaje `attackPower * 2` obrazen. To wlasnie **polimorfizm** -- smok atakuje po swojemu!
- **`getAttackPower()`** -- uzywamy gettera zamiast bezposredniego dostepu do `attackPower`, bo pole jest `private` w Entity.

### Lancuch dziedziczenia wizualnie:

```
        Entity
       /      \
    Player   Monster
               |
             Dragon
```

Kazda klasa dziedziczy wszystko od rodzica i moze dodac swoje rzeczy lub nadpisac (`@Override`) zachowanie.

---

## Krok 5: Arena -- niech rozpocznie sie walka!

Czas na najlepszy moment -- arena, na ktorej nasi bohaterowie beda walczyc!

Stworz plik `Arena.java` (w glownym pakiecie, nie w `entity/`):

```java
package pl.codingtree.javanluzie;

import pl.codingtree.javanluzie.entity.Entity;
import pl.codingtree.javanluzie.entity.Monster;
import pl.codingtree.javanluzie.entity.Player;

public class Arena {

    public static void fight(Entity fighter1, Entity fighter2) {
        System.out.println("\n========================================");
        System.out.println("  ARENA: " + fighter1.getName() + " vs " + fighter2.getName());
        System.out.println("========================================\n");

        int round = 1;
        while (fighter1.isAlive() && fighter2.isAlive()) {
            System.out.println("--- Runda " + round + " ---");
            fighter1.attack(fighter2);
            if (fighter2.isAlive()) {
                fighter2.attack(fighter1);
            }
            System.out.println(fighter1);
            System.out.println(fighter2);
            System.out.println();
            round++;
        }

        Entity winner = fighter1.isAlive() ? fighter1 : fighter2;
        System.out.println("🏆 " + winner.getName() + " wygrywa! 🏆");

        if (winner instanceof Player player && fighter2 instanceof Monster monster) {
            player.gainExperience(50);
            monster.dropLoot();
        }
    }
}
```

### Magia polimorfizmu w akcji!

Spojrz na sygnature metody: `fight(Entity fighter1, Entity fighter2)`. Przyjmuje **dwa Entity**. Nie obchodzi ja, czy to Player, Monster, czy Dragon. Dzieki polimorfizmowi:

- Gdy `fighter1` to Player, `attack()` dziala normalnie.
- Gdy `fighter2` to Dragon, `attack()` zieje ogniem!
- Java sama wie, ktora wersje `attack()` wywolac. To sie nazywa **dynamic dispatch** -- wywolanie wlasciwej metody w czasie dzialania programu.

### Inne ciekawostki:

- **`fighter1.isAlive() ? fighter1 : fighter2`** -- operator trojargumentowy (ternary). To skrocony if-else. "Jesli fighter1 zyje, to zwyciezca jest fighter1, w przeciwnym razie fighter2".
- **`instanceof Player player`** -- sprawdza typ I od razu tworzy zmienna. "Jesli winner jest Playerem, nazwij go `player`". To pattern matching dodany w nowszych wersjach Javy.

---

## Krok 6: Main -- odpalamy gre!

Zaktualizuj plik `Main.java`:

```java
package pl.codingtree.javanluzie;

import pl.codingtree.javanluzie.entity.Dragon;
import pl.codingtree.javanluzie.entity.Player;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Java na Luzie - Sesja 1 ===\n");

        Player hero = new Player("Rycerz", 100, 15);
        Dragon dragon = new Dragon("Smaug", 80, 10);

        System.out.println("Nasi wojownicy:");
        System.out.println(hero);
        System.out.println(dragon);

        Arena.fight(hero, dragon);
    }
}
```

### Uruchom program!

W terminalu, w folderze `project/`:

```bash
mvn compile exec:java -Dexec.mainClass="pl.codingtree.javanluzie.Main"
```

Powinienes zobaczyc epicka walke miedzy Rycerzem a Smaugiem! Kazde uruchomienie moze sie skonczyc inaczej, bo Monster (a wiec i Dragon... chociaz Dragon nadpisal atak, wiec akurat nie pudluje!) ma losowe pudla.

---

## Podsumowanie -- czego sie dzis nauczyles

| Koncept | Co to robi |
|---|---|
| **Klasa** | Blueprint/przepis na obiekt |
| **Pola (fields)** | Dane obiektu (`private String name`) |
| **Konstruktor** | Tworzy obiekt (`new Player(...)`) |
| **Metody** | Zachowanie obiektu (`attack()`, `takeDamage()`) |
| **Enkapsulacja** | Ukrywanie pol (`private` + gettery) |
| **Dziedziczenie** | `extends` -- klasa potomna dziedziczy od rodzica |
| **`super`** | Wywolanie konstruktora/metody z klasy nadrzednej |
| **Polimorfizm** | Ta sama metoda, rozne zachowanie w roznych klasach |
| **`@Override`** | Adnotacja -- "nadpisuje metode z rodzica" |
| **`toString()`** | Jak obiekt wyswietla sie jako tekst |
| **`instanceof`** | Sprawdzanie typu obiektu |

---

## BOSS FIGHT CHALLENGE: Klasa Healer

Czas na wyzwanie! Stworz klase `Healer` -- uleczyciel, ktory potrafi leczyc innych bohaterow.

### Wymagania:

1. `Healer` dziedziczy po `Player` (bo to tez gracz).
2. Ma dodatkowe pole `int healPower` -- sila leczenia.
3. Ma metode `heal(Entity target)` ktora:
   - Zwieksza HP celu o `healPower` (ale nie ponad `maxHealth`!).
   - Wypisuje komunikat kto kogo uleczy i ile HP przywrocil.
4. Nadpisz `toString()` zeby wyswietlal tez `healPower`.

### Podpowiedzi:

- Bedziesz potrzebowal dodac **setter** do pola `health` w Entity, albo nowa metode `heal(int amount)` w Entity. Pomysl, ktore rozwiazanie jest lepsze i dlaczego!
- Pamietaj o `@Override` i `super`.
- Zeby sprawdzic `maxHealth` z Healera, uzyj `getMaxHealth()`.

### Bonus:

Zmodyfikuj `Main.java` tak, zeby Healer leczyl Rycerza po kazdej rundzie walki. Zmien `Arena.fight()` albo napisz nowa wersje walki z supportem!

Powodzenia, wojowniku! W nastepnej sesji zajmiemy sie kolekcjami i ekwipunkiem bohatera.
