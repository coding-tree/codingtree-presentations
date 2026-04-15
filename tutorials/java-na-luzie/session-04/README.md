# Sesja 4: Supermoc Javy

> **Java na Luzie** -- kurs Javy dla tych, co chca kiedys zbudowac wlasnego Minecrafta.

Ostatnia sesja. Masz juz bohaterow, ekwipunek, crafting i silnik gry. Dzisiaj dodajemy cos, co sprawia, ze Java staje sie **naprawde potezna** -- nowoczesne narzedzia, ktore profesjonalni programisci uzywaja codziennie.

Wyobraz sobie, ze twoj silnik gry dziala, ale masz problem: cos nie dziala, a jedyne co widzisz to... nic. Zero informacji. `System.out.println` rozrzucone po kodzie. Brak kolorow, brak poziomow waznosci, brak mozliwosci wylaczenia niepotrzebnych komunikatow.

Dzisiaj to naprawimy -- i przy okazji nauczymy sie pisac kod, ktory jest **krotszy, czytelniejszy i bardziej elegancki** niz cokolwiek, co widziales do tej pory.

Gotowy na supermoc? Lecimy!

---

## Koniec z System.out.println!

Przez trzy sesje uzywalismy `System.out.println()` do wyswietlania tekstu. To dziala, ale ma powaZne problemy:

- Nie wiesz, czy komunikat to **blad**, **ostrzezenie**, czy po prostu **informacja**
- Nie mozesz latwo wlaczyc/wylaczyc komunikatow debugowych
- Brak kolorow -- wszystko jest bialo-czarne
- Brak daty/czasu -- nie wiesz, kiedy cos sie stalo

Profesjonalni programisci (takze ci robiacy Minecrafta) uzywaja **loggerow**. My uzyJemy **SLF4J** z **Logback** -- najpopularniejszego zestawu w swiecie Javy.

### Czym jest logger?

Logger to inteligentny `System.out.println`. Zamiast:
```java
System.out.println("Gracz otrzymal obrazenia");
```

Piszesz:
```java
logger.info("Gracz otrzymal obrazenia");
```

Roznica? Logger:
- Dodaje **timestamp** (godzine)
- Dodaje **kolorowy poziom** (DEBUG, INFO, WARN, ERROR)
- Dodaje **nazwe klasy**, ktora wyslala komunikat
- Mozesz wylaczac cale grupy komunikatow **bez usuwania kodu**

### Poziomy logowania

```
DEBUG   -- szczegoly techniczne (obliczenia walki, delta time)
INFO    -- wazne wydarzenia (start gry, zwyciEstwo, lup)
WARN    -- ostrzezenia (niskie HP, pelny ekwipunek)
ERROR   -- bledy (crashe, brakujace pliki)
```

Wyobraz sobie to jak radio w samochodzie. DEBUG to szum na kazdej czestotliwosci. INFO to wiadomosci. WARN to komunikat o korku. ERROR to syrena alarmowa. Mozesz ustawic glosnosc -- jesli ustawisz na INFO, nie uslyszysz DEBUG. Jesli na ERROR -- uslyszysz tylko bledy.

### Python vs Java

```python
# Python -- modul logging
import logging
logger = logging.getLogger(__name__)
logger.info("Gracz dostal %d XP", 50)
```

```java
// Java -- SLF4J + Logback
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger logger = LoggerFactory.getLogger(MojaKlasa.class);
logger.info("Gracz dostal {} XP", 50);
```

Zwroc uwage: w Javie uzywamy `{}` jako placeholder (zamiast `%d` w Pythonie). SLF4J sam wstawia wartosci -- to jest szybsze niz sklejanie Stringow.

---

## Krok 1: Konfiguracja Logback

Logback potrzebuje pliku konfiguracyjnego. Tworzymy `src/main/resources/logback.xml`:

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss} %highlight(%-5level) %cyan(%logger{15}) - %msg%n</pattern>
        </encoder>
    </appender>
    <root level="DEBUG">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

Co tu jest?

- `%d{HH:mm:ss}` -- godzina, minuta, sekunda
- `%highlight(%-5level)` -- kolorowy poziom (ERROR = czerwony, WARN = zolty, INFO = niebieski, DEBUG = szary)
- `%cyan(%logger{15})` -- nazwa klasy w kolorze cyan, max 15 znakow
- `%msg%n` -- twoja wiadomosc + nowa linia

Gdy odpalasz program, zamiast nudnego:
```
Gracz atakuje Goblina
Goblin otrzymuje 15 obrazen
```

Widzisz:
```
14:32:01 INFO  e.Entity        - Rycerz atakuje Goblin!
14:32:01 DEBUG e.Entity        - Goblin otrzymuje 15 obrazen! HP: 15/30
14:32:01 WARN  e.Entity        - Goblin ma malo HP! (15/30)
```

Kolorowe, z czasem, z nazwa klasy. Profesjonalnie!

---

## Krok 2: Dodajemy logger do istniejacych klas

### Entity.java -- inteligentne logowanie obrazen

Dodajemy logger na gorze klasy:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Entity implements Renderable, Updatable {
    private static final Logger logger = LoggerFactory.getLogger(Entity.class);
    // ... reszta pol
```

Teraz metoda `takeDamage` staje sie madrZejsza:

```java
public void takeDamage(int damage) {
    health -= damage;
    if (health < 0) health = 0;
    logger.debug("{} otrzymuje {} obrazen! HP: {}/{}", name, damage, health, maxHealth);

    // Ostrzezenie gdy HP ponizej 20%
    if (health > 0 && health < maxHealth * 0.2) {
        logger.warn("{} ma malo HP! ({}/{})", name, health, maxHealth);
    }
}
```

Widzisz te roznice?
- Normalne obrazenia to `DEBUG` -- szczegoly techniczne
- Niskie HP to `WARN` -- ostrzezenie, ze ktos zaraz zginie
- `{}` zamiast sklejania Stringow plusami -- czytelniejsze i szybsze

### Arena.java -- kolorowe walki

```java
private static final Logger logger = LoggerFactory.getLogger(Arena.class);

public static void fight(Entity fighter1, Entity fighter2) {
    logger.info("========================================");
    logger.info("  ARENA: {} vs {}", fighter1.getName(), fighter2.getName());
    logger.info("========================================");

    int round = 1;
    while (fighter1.isAlive() && fighter2.isAlive()) {
        logger.debug("--- Runda {} ---", round);
        // ... walka
    }

    Entity winner = fighter1.isAlive() ? fighter1 : fighter2;
    logger.info("{} wygrywa!", winner.getName());
}
```

### GameLoop.java i GameWorld.java

Te same zmiany -- zamieniamy `System.out.println` na odpowiedni poziom:

```java
// GameLoop
logger.info("=== {} -- START ===", config.title());
logger.debug("--- Tick {} (dt: {}s) ---", tick, String.format("%.4f", deltaTime));
logger.warn("Swiat jest pusty! Koniec gry.");
logger.info("=== GAME OVER ===");

// GameWorld
logger.info("[World] Dodano: {}", entity.getName());
logger.warn("[World] Swiat pelny! Max: {}", config.maxEntities());
logger.info("[World] Usunieto: {}", entity.getName());
logger.debug("[Debug] Entities: {}", entities.size());
```

---

## Krok 3: Lambdy -- krotszy kod, wieksza moc

Teraz cos naprawde fajnego. PamiEtasz Pythona?

```python
# Python lambda
podwoj = lambda x: x * 2
print(podwoj(5))  # 10
```

Java ma cos bardzo podobnego:

```java
// Java lambda
Function<Integer, Integer> podwoj = x -> x * 2;
System.out.println(podwoj.apply(5)); // 10
```

Ale lambdy w Javie sa **znacznie potezniejsze** niz w Pythonie. W Pythonie lambda moze miec tylko jedno wyrazenie. W Javie -- caly blok kodu.

### Od klasy anonimowej do lambdy

Wyobraz sobie, ze chcesz posortowac potworki po HP. Bez lambdy:

```java
// Java bez lambdy (stary styl) -- BRZYDKIE
monsters.sort(new Comparator<Monster>() {
    @Override
    public int compare(Monster a, Monster b) {
        return Integer.compare(a.getHealth(), b.getHealth());
    }
});
```

Z lambda:

```java
// Java z lambda -- LADNE
monsters.sort((a, b) -> Integer.compare(a.getHealth(), b.getHealth()));
```

Z method reference -- jeszcze ladniej:

```java
// Java z method reference -- NAJLADNIEJSZE
monsters.sort(Comparator.comparingInt(Monster::getHealth));
```

### forEach -- zapomnij o petli for

```java
// Stary styl
for (Monster m : monsters) {
    System.out.println(m);
}

// Nowy styl z lambda
monsters.forEach(m -> logger.info("  - {}", m));
```

Krotsze, czytelniejsze, eleganckie.

### Python vs Java -- porownanie lambd

```python
# Python
lista = [1, 2, 3, 4, 5]
parzyste = list(filter(lambda x: x % 2 == 0, lista))
podwojone = list(map(lambda x: x * 2, lista))
```

```java
// Java
List<Integer> lista = List.of(1, 2, 3, 4, 5);
List<Integer> parzyste = lista.stream()
    .filter(x -> x % 2 == 0)
    .toList();
List<Integer> podwojone = lista.stream()
    .map(x -> x * 2)
    .toList();
```

Wygladaja podobnie! Ale Java ma jeszcze jedna superbrOn...

---

## Krok 4: Streamy -- potoki danych

Stream to "potok" przez ktory przeplywaja dane. Wyobraz sobie tasme w fabryce:

```
[Goblin, Ork, Dragon, Zombie, Goblin]
        |
    filter(isAlive)      -- usun martwych
        |
    map(getName)         -- weZ tylko imiona
        |
    sorted()             -- posortuj alfabetycznie
        |
    toList()             -- zbierz do listy
        |
["Dragon", "Goblin", "Ork"]
```

### Przyklad: ekwipunek

```java
// Znajdz wszystkie legendarne przedmioty
List<String> legendarne = inventory.getItems().stream()
    .filter(item -> item.getRarity() == Rarity.LEGENDARY)
    .map(Item::getName)
    .sorted()
    .toList();

// Policz laczna wage
double totalWeight = inventory.getItems().stream()
    .mapToDouble(Item::getWeight)
    .sum();

// Grupuj przedmioty po rzadkosci
Map<Rarity, Long> byRarity = inventory.getItems().stream()
    .collect(Collectors.groupingBy(Item::getRarity, Collectors.counting()));
```

### Przyklad: walka

```java
// Ile potworow zyje?
long alive = entities.stream()
    .filter(Entity::isAlive)
    .count();

// Kto jest najsilniejszy?
Optional<Entity> strongest = entities.stream()
    .filter(Entity::isAlive)
    .max(Comparator.comparingInt(Entity::getAttackPower));

// Zbierz lup z pokonanych potworow
List<String> loot = monsters.stream()
    .filter(m -> !m.isAlive())
    .map(Monster::dropLoot)
    .toList();
```

---

## Krok 5: Optional -- koniec z NullPointerException!

W Pythonie czesto piszesz:

```python
monster = find_monster("Goblin")
if monster is not None:
    monster.attack(player)
```

W Javie stary styl wyglada tak:

```java
Monster monster = findMonster("Goblin");
if (monster != null) {
    monster.attack(player);
}
```

Problem? Jesli zapomnisz sprawdzic `null`, dostajesz `NullPointerException` -- najczestszy blad w historii Javy. Miliony programistow, miliardy crashy.

`Optional` to pudelko, ktore moze zawierac wartosC albo byc puste:

```java
Optional<Entity> strongest = entities.stream()
    .filter(Entity::isAlive)
    .max(Comparator.comparingInt(Entity::getAttackPower));

// Zamiast sprawdzac null:
strongest.ifPresent(e ->
    logger.info("Najsilniejszy: {} (ATK: {})", e.getName(), e.getAttackPower())
);

// Albo z wartoscia domyslna:
String name = strongest
    .map(Entity::getName)
    .orElse("Brak zywych encji");
```

`Optional` mowi ci WPROST: "ta wartosc moze nie istniec -- obsluz to". Zaden crash, zero niespodzianek.

---

## Krok 6: Referencje do metod -- `Entity::isAlive`

Widziales juz `Entity::isAlive` w przykladach powyzej. To **method reference** -- skrot dla lambdy:

```java
// Lambda
.filter(e -> e.isAlive())

// Method reference (to samo, krociej)
.filter(Entity::isAlive)
```

Dziala tez z:
- **Statycznymi metodami:** `Integer::parseInt`
- **Metodami instancji:** `monster::dropLoot`
- **Konstruktorami:** `ArrayList::new`
- **Loggerem:** `logger::info`

```java
// Zamiast:
names.forEach(name -> logger.info(name));

// Mozesz:
names.forEach(logger::info);
```

---

## Krok 7: Interfejsy funkcyjne

Lambda potrzebuje "typu". Java definiuje kilka gotowych:

| Interfejs | Co robi | Przyklad |
|-----------|---------|----------|
| `Predicate<T>` | Test (T -> boolean) | `Entity::isAlive` |
| `Consumer<T>` | Uzyj (T -> void) | `logger::info` |
| `Function<T,R>` | Przeksztalc (T -> R) | `Item::getName` |
| `Supplier<T>` | Stworz (() -> T) | `ArrayList::new` |
| `Comparator<T>` | Porownaj (T,T -> int) | `(a,b) -> a.getHealth() - b.getHealth()` |

Nie musisz ich wszystkich pamietac -- IDE podpowie. Wazne, zebys wiedzial, ze **istnieja** i ze lambdy to nie magia, tylko implementacja tych interfejsow.

---

## Krok 8: Klasa Statistics -- boss fight ze streamami

Czas na boss fighta tej sesji! Tworzymy klase `Statistics`, ktora uzywa WSZYSTKIEGO czego sie dzisiaj nauczylismy:

```java
package pl.codingtree.javanluzie.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codingtree.javanluzie.entity.Entity;
import pl.codingtree.javanluzie.inventory.Inventory;
import pl.codingtree.javanluzie.item.Item;
import pl.codingtree.javanluzie.item.Rarity;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private static final Logger logger = LoggerFactory.getLogger(Statistics.class);

    public static void printEntityStats(List<Entity> entities) {
        logger.info("=== Statystyki Encji ===");

        // Stream: policz zywych
        long alive = entities.stream()
                .filter(Entity::isAlive)        // method reference!
                .count();
        logger.info("Zyje: {}/{}", alive, entities.size());

        // Stream + Optional: najsilniejszy
        Optional<Entity> strongest = entities.stream()
                .filter(Entity::isAlive)
                .max(Comparator.comparingInt(Entity::getAttackPower));
        strongest.ifPresent(e ->
                logger.info("Najsilniejszy: {} (ATK: {})",
                    e.getName(), e.getAttackPower()));

        // Stream + Optional: najwiecej HP
        Optional<Entity> mostHealth = entities.stream()
                .filter(Entity::isAlive)
                .max(Comparator.comparingInt(Entity::getHealth));
        mostHealth.ifPresent(e ->
                logger.info("Najwiecej HP: {} ({}/{})",
                    e.getName(), e.getHealth(), e.getMaxHealth()));

        // Stream + sorted + forEach: ranking
        entities.stream()
                .filter(Entity::isAlive)
                .sorted(Comparator.comparingInt(Entity::getHealth).reversed())
                .forEach(e -> logger.debug("  {} - HP: {}",
                    e.getName(), e.getHealth()));
    }

    public static void printInventoryStats(Inventory<Item> inventory) {
        logger.info("=== Statystyki Ekwipunku ===");

        List<Item> items = inventory.getItems();
        logger.info("Przedmiotow: {}/{}", items.size(), inventory.getMaxSize());

        // Stream: laczna waga
        double totalWeight = items.stream()
                .mapToDouble(Item::getWeight)
                .sum();
        logger.info("Laczna waga: {}kg", String.format("%.1f", totalWeight));

        // Stream + Collectors.groupingBy: grupowanie po rzadkosci
        Map<Rarity, Long> byRarity = items.stream()
                .collect(Collectors.groupingBy(
                    Item::getRarity, Collectors.counting()));
        byRarity.forEach((rarity, count) ->
                logger.info("  {}: {}", rarity.getDisplayName(), count));

        // Stream + filter + map: legendarne przedmioty
        items.stream()
                .filter(i -> i.getRarity() == Rarity.LEGENDARY)
                .map(Item::getName)
                .sorted()
                .forEach(name -> logger.warn("LEGENDARNY: {}", name));
    }
}
```

Przeanalizujmy to:

1. **`filter(Entity::isAlive)`** -- filtruje tylko zywych (method reference do `isAlive()`)
2. **`max(Comparator.comparingInt(...))`** -- zwraca `Optional` -- moze nie byc zywych!
3. **`ifPresent(e -> ...)`** -- wykonaj lambda TYLKO jesli Optional cos zawiera
4. **`sorted(...).reversed()`** -- sortuj malejaco
5. **`collect(Collectors.groupingBy(...))`** -- grupuj i policz -- jak SQL GROUP BY!
6. **`mapToDouble(...).sum()`** -- przeksztalc na double i zsumuj

Kazda linijka to osobna "stacja" na tasmie produkcyjnej. Dane wchodza z jednej strony, wynik wychodzi z drugiej. Zero petli for, zero zmiennych tymczasowych.

---

## Krok 9: Nowy Main.java -- demo wszystkiego

```java
package pl.codingtree.javanluzie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.codingtree.javanluzie.entity.*;
import pl.codingtree.javanluzie.inventory.Inventory;
import pl.codingtree.javanluzie.item.*;
import pl.codingtree.javanluzie.stats.Statistics;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("=== Java na Luzie - Sesja 4: Supermoc Javy ===");

        // Tworzymy bohaterow
        Player hero = new Player("Rycerz", 100, 15);
        List<Monster> monsters = new ArrayList<>(List.of(
            new Monster("Goblin", 30, 5, "Maly Noz"),
            new Monster("Ork", 50, 8, "Topor"),
            new Dragon("Smaug", 80, 10)
        ));

        // Lambda: forEach
        logger.info("Nasi przeciwnicy:");
        monsters.forEach(m -> logger.info("  - {}", m));

        // Walki
        List<Entity> allEntities = new ArrayList<>();
        allEntities.add(hero);
        allEntities.addAll(monsters);

        for (Monster monster : monsters) {
            if (hero.isAlive()) {
                Arena.fight(hero, monster);
            }
        }

        // Streamy na encjach
        logger.info("--- Stream magic ---");
        long deadCount = monsters.stream()
                .filter(m -> !m.isAlive())
                .count();
        logger.info("Pokonani potworzy: {}", deadCount);

        // Zbierz lup z pokonanych
        List<String> loot = monsters.stream()
                .filter(m -> !m.isAlive())
                .map(Monster::dropLoot)
                .toList();
        logger.info("Zebrano lup: {}", loot);

        // Ekwipunek ze streamami
        Inventory<Item> plecak = new Inventory<>("Plecak", 10);
        plecak.add(new Weapon("Miecz Ognia", 3.0, Rarity.EPIC, 25));
        plecak.add(new Weapon("Sztylet", 1.0, Rarity.COMMON, 8));
        plecak.add(new Armor("Tarcza Smoka", 5.0, Rarity.LEGENDARY, 30));
        plecak.add(new Item("Mikstura HP", 0.3, Rarity.UNCOMMON));
        plecak.add(new Item("Zloto", 0.1, Rarity.RARE));

        // Statystyki -- boss fight ze streamami!
        Statistics.printEntityStats(allEntities);
        Statistics.printInventoryStats(plecak);

        logger.info("=== Koniec sesji 4 ===");
        logger.info("Gotowy na LWJGL? Twoja przygoda dopiero sie zaczyna!");
    }
}
```

---

## Podsumowanie

W tej sesji nauczyles sie:

| Temat | Co robi | Przyklad |
|-------|---------|----------|
| **SLF4J + Logback** | Profesjonalne logowanie z kolorami i poziomami | `logger.info("HP: {}", hp)` |
| **Lambdy** | Krotkie funkcje "w locie" | `x -> x * 2` |
| **Streamy** | Potoki przetwarzania kolekcji | `list.stream().filter(...).map(...).toList()` |
| **Optional** | Bezpieczna obsluga brakujacych wartosci | `optional.ifPresent(x -> ...)` |
| **Method references** | Skrot dla prostych lambd | `Entity::isAlive` |
| **Interfejsy funkcyjne** | Typy dla lambd | `Predicate`, `Consumer`, `Function` |

### Mapa calego kursu

```
Sesja 1: Stworz Swoj Swiat     -- klasy, obiekty, dziedziczenie
Sesja 2: Ekwipunek Bohatera    -- kolekcje, generics, enumy, crafting
Sesja 3: Silnik Gry            -- interfejsy, record, game loop
Sesja 4: Supermoc Javy         -- logowanie, lambdy, streamy, Optional
```

### Co dalej?

Masz teraz solidne fundamenty Javy. Nastepne kroki to:

- **LWJGL** -- biblioteka graficzna (OpenGL w Javie), ta sama co Minecraft
- **JavaFX** -- tworzenie okienkowych aplikacji z GUI
- **Spring Boot** -- backend webowy (serwery, API, bazy danych)
- **JUnit** -- testy automatyczne (sprawdzanie czy kod dziala)

Kazdy z tych kierunkow buduje na tym, czego sie nauczyles. Klasy, interfejsy, generics, streamy -- to wszystko wraca. Gratulacje -- jestes na dobrej drodze do zostania Java developerem!
