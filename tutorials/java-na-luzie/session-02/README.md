# Sesja 2: Ekwipunek Bohatera

> **Java na Luzie** -- kurs Javy dla tych, co chca kiedys zbudowac wlasnego Minecrafta.

Witaj ponownie, wojowniku! W poprzedniej sesji stworzyliSmy bohaterow, potwory i arene walki. Ale co to za RPG bez ekwipunku? Dzis nasz Rycerz dostanie plecak, bronie, zbroje, a nawet stol do craftowania -- zupelnie jak w Minecrafcie!

Po drodze nauczysz sie kilku poteznych narzedzi Javy: **enumow**, **generykow**, **kompozycji** i tajemniczego duetu **equals/hashCode**. Brzmi strasznie? Spokojnie -- to bedzie na luzie.

---

## Enumy -- lepsze niz stringi

W Pythonie, gdybys chcial okreslic rzadkosc przedmiotu, pewnie zrobilbys tak:

```python
rarity = "legendary"
```

Problem? Mozesz napisac `"legendry"` albo `"LEGENDARY"` albo `"leg"` -- Python nie protestuje. Blad zobaczysz dopiero w trakcie gry, gdy twoj legendarny miecz nie zadziala.

W Javie mamy **enumy** -- specjalny typ, ktory definiuje **zamkniety zbior wartosci**. Kompilator pilnuje, zebys uzywal tylko tych, ktore zdefiniowales.

Stworz plik `item/Rarity.java`:

```java
package pl.codingtree.javanluzie.item;

public enum Rarity {
    COMMON("Zwykly", "white"),
    UNCOMMON("Niezwykly", "green"),
    RARE("Rzadki", "blue"),
    EPIC("Epicki", "purple"),
    LEGENDARY("Legendarny", "orange");

    private final String displayName;
    private final String color;

    Rarity(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
}
```

### Co tu sie dzieje?

- **`enum`** zamiast `class` -- mowimy Javie: "Rarity moze byc TYLKO jedna z tych pieciu wartosci".
- Kazda wartosc (`COMMON`, `UNCOMMON`, ...) to **obiekt** -- ma swoje pola (`displayName`, `color`) i metody.
- Enum ma konstruktor, ale NIE mozesz go wywolac z zewnatrz -- `new Rarity(...)` nie zadziala. Wartosci tworza sie same.
- Pisownia WIELKIMI_LITERAMI to konwencja -- stale w Javie zawsze tak wygladaja.

### Dlaczego to lepsze niz String?

Sprobuj napisac `Rarity.LEGENDRY` -- kompilator wyrzuci blad! Nie ma takiej wartosci. Enum gwarantuje, ze nigdy nie popelnisz literowki w rzadkosci przedmiotu.

```java
Rarity r = Rarity.LEGENDARY;  // OK
Rarity r = Rarity.LEGENDRY;   // BLAD KOMPILACJI! Kompilator cie ratuje
```

W Pythonie `rarity = "legendry"` -- cisza, zero bledu. Bug czeka na ciebie w najgorszym momencie.

---

## Krok 1: Item -- przedmiot bazowy

Kazdy przedmiot w grze ma nazwe, wage i rzadkosc. Zacznijmy od klasy bazowej.

Stworz plik `item/Item.java`:

```java
package pl.codingtree.javanluzie.item;

import java.util.Objects;

public class Item {
    private final String name;
    private final double weight;
    private final Rarity rarity;

    public Item(String name, double weight, Rarity rarity) {
        this.name = name;
        this.weight = weight;
        this.rarity = rarity;
    }

    public String getName() { return name; }
    public double getWeight() { return weight; }
    public Rarity getRarity() { return rarity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && rarity == item.rarity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rarity);
    }

    @Override
    public String toString() {
        return name + " [" + rarity.getDisplayName() + ", " + weight + "kg]";
    }
}
```

### Nowosci:

- **`final`** przy polach -- raz ustawione, nie mozna ich zmienic. Przedmiot po stworzeniu ma stala nazwe, wage i rzadkosc. To sie nazywa **niemutowalnosc (immutability)** -- obiekt, ktory sie nie zmienia, jest bezpieczniejszy.
- **`double`** -- typ zmiennoprzecinkowy (z kropka). Waga moze byc 0.5 kg.
- **`equals()` i `hashCode()`** -- o tym za chwile, to wazna sprawa!

---

## Krok 2: Weapon i Armor -- dziedziczenie raz jeszcze

Bron to Item, ale z dodatkowym polem -- obrazeniami. Zbroja to Item z obrona. Znasz juz `extends` z sesji 1, wiec to bedzie szybkie.

Stworz plik `item/Weapon.java`:

```java
package pl.codingtree.javanluzie.item;

public class Weapon extends Item {
    private final int damage;

    public Weapon(String name, double weight, Rarity rarity, int damage) {
        super(name, weight, rarity);
        this.damage = damage;
    }

    public int getDamage() { return damage; }

    @Override
    public String toString() {
        return super.toString() + " DMG: " + damage;
    }
}
```

Stworz plik `item/Armor.java`:

```java
package pl.codingtree.javanluzie.item;

public class Armor extends Item {
    private final int defense;

    public Armor(String name, double weight, Rarity rarity, int defense) {
        super(name, weight, rarity);
        this.defense = defense;
    }

    public int getDefense() { return defense; }

    @Override
    public String toString() {
        return super.toString() + " DEF: " + defense;
    }
}
```

### Kiedy dziedziczenie ma sens?

Weapon **jest** Itemem (z dodatkowym polem). Armor **jest** Itemem (z dodatkowym polem). To klasyczny przypadek, gdzie dziedziczenie jest idealne -- mowimy "A jest specjalnym rodzajem B".

Ale uwaga! Nie wszystko powinno dziedziczyc. Wyobraz sobie:

```
Item
 ├── Weapon
 ├── Armor
 ├── MagicWeapon (ma damage I spell?)
 ├── EnchantedArmor (ma defense I spell?)
 └── MagicWeaponArmor (??? ma wszystko?)
```

Widzisz problem? Gdy zaczniesz kombinowac "bron z magia" i "zbroja z magia", dziedziczenie sie komplikuje. Java nie pozwala dziedziczyc po wielu klasach naraz (w przeciwienstwie do Pythona!).

Rozwiazanie? **Kompozycja** -- zamiast "jest", mysl "ma". Przedmiot **ma** efekt magiczny. Zobaczymy to przy craftowaniu!

---

## Krok 3: Generyki -- skrzynka na wszystko (albo nie)

Czas na ekwipunek! Ale zanim zaczniemy -- wyobraz sobie taka sytuacje:

- Masz plecak bohatera -- moze trzymac bronie, zbroje, materialy, wszystko.
- Masz stojak na bronie -- moze trzymac TYLKO bronie.
- Masz stojak na zbroje -- moze trzymac TYLKO zbroje.

Czy musisz pisac trzy rozne klasy? `GeneralInventory`, `WeaponInventory`, `ArmorInventory`? To bylby koszmar!

W Javie mamy **generyki** -- mowisz klasie: "bedziesz przechowywac TEN typ, ktory ci podam przy tworzeniu".

Stworz plik `inventory/Inventory.java`:

```java
package pl.codingtree.javanluzie.inventory;

import pl.codingtree.javanluzie.item.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory<T extends Item> {
    private final String name;
    private final List<T> items;
    private final int maxSize;

    public Inventory(String name, int maxSize) {
        this.name = name;
        this.items = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public boolean add(T item) {
        if (items.size() >= maxSize) {
            System.out.println("Ekwipunek " + name + " jest pelny!");
            return false;
        }
        items.add(item);
        System.out.println("+ " + item.getName() + " dodano do " + name);
        return true;
    }

    public boolean remove(T item) {
        boolean removed = items.remove(item);
        if (removed) {
            System.out.println("- " + item.getName() + " usunieto z " + name);
        }
        return removed;
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public List<T> getItems() {
        return List.copyOf(items);
    }

    public int getSize() { return items.size(); }
    public int getMaxSize() { return maxSize; }
    public String getName() { return name; }

    public void show() {
        System.out.println("\n=== " + name + " (" + items.size() + "/" + maxSize + ") ===");
        for (T item : items) {
            System.out.println("  " + item);
        }
    }
}
```

### Generyki -- rozkladamy na czesci

**`Inventory<T extends Item>`** -- co to znaczy?

- `T` to **parametr typu** -- placeholder na konkretny typ. Jak zmienna, ale dla typow!
- `extends Item` -- ograniczenie: T musi byc Itemem lub jego podklasa (Weapon, Armor...).
- Gdy tworzysz obiekt, podajesz konkretny typ w `< >`:

```java
Inventory<Item> plecak = new Inventory<>("Plecak", 10);        // przyjmuje wszystko
Inventory<Weapon> stojakNaBronie = new Inventory<>("Bronie", 5); // TYLKO bronie!
Inventory<Armor> stojakNaZbroje = new Inventory<>("Zbroje", 5);  // TYLKO zbroje!
```

Teraz sprobuj:
```java
stojakNaBronie.add(new Armor("Tarcza", 5.0, Rarity.COMMON, 10)); // BLAD KOMPILACJI!
```

Kompilator nie pozwoli wlozyc zbroi do stojaka na bronie. **Bezpieczenstwo typow** w akcji!

### Python tego nie ma!

W Pythonie moglbys wrzucic cokolwiek do listy -- stringa, liczbe, smoka. Zero kontroli. Dopiero w runtime zobaczysz, ze cos nie gra. Java z generykami lapie takie bledy **w czasie kompilacji**.

### ArrayList i List -- dwie strony medalu

- **`List<T>`** -- to **interfejs**. Mowi "co" obiekt umie robic (add, remove, get...), ale nie jak.
- **`ArrayList<T>`** -- to **implementacja**. Mowi "jak" -- uzywaj tablicy, ktora sie rozrasta.

Dlaczego piszemy `List<T> items = new ArrayList<>()`? Bo deklarujemy typ jako interfejs (elastycznosc), a tworzymy konkretna implementacje. Gdybys chcial zmienic na `LinkedList` -- zmienisz w jednym miejscu.

### `List.copyOf(items)` -- obrona przed oszustwem

W metodzie `getItems()` zwracamy **kopie** listy, nie oryginal. Dlaczego? Gdybys zwrocil oryginal, ktos moglby zrobic:

```java
inventory.getItems().clear(); // OOPS, wyczyscil caly ekwipunek!
```

`List.copyOf()` tworzy niemutowalna kopie -- probra jej zmienic wyrzuci wyjatek.

---

## Krok 4: Recipe -- kompozycja w akcji

Przepis (Recipe) to swietny przyklad **kompozycji**. Przepis NIE jest przedmiotem -- nie mozesz go wlozyc do plecaka. Przepis **zawiera** liste przedmiotow (skladnikow) i produkuje nowy przedmiot.

Stworz plik `crafting/Recipe.java`:

```java
package pl.codingtree.javanluzie.crafting;

import pl.codingtree.javanluzie.item.Item;
import java.util.List;

public class Recipe {
    private final String name;
    private final List<Item> ingredients;
    private final Item result;

    public Recipe(String name, List<Item> ingredients, Item result) {
        this.name = name;
        this.ingredients = List.copyOf(ingredients);
        this.result = result;
    }

    public String getName() { return name; }
    public List<Item> getIngredients() { return ingredients; }
    public Item getResult() { return result; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Przepis: ").append(name).append("\n");
        sb.append("  Skladniki: ");
        for (Item i : ingredients) {
            sb.append(i.getName()).append(", ");
        }
        sb.append("\n  Wynik: ").append(result);
        return sb.toString();
    }
}
```

### Kompozycja vs Dziedziczenie

- **Dziedziczenie**: "Recipe JEST Itemem" -- nie! Przepis to nie przedmiot.
- **Kompozycja**: "Recipe MA liste Itemow" -- tak! Przepis **zawiera** skladniki.

Zasada: **Uzywaj dziedziczenia dla relacji "jest", kompozycji dla relacji "ma".**

Weapon jest Itemem -> `extends Item`
Recipe ma Itemy -> `private List<Item> ingredients`

### StringBuilder -- laczenie stringow po bozemu

W `toString()` uzywamy `StringBuilder` zamiast `+`. Dlaczego? Kazde `+` na stringach tworzy nowy obiekt String w pamieci. StringBuilder modyfikuje jeden bufor. Dla 2-3 elementow to nie ma znaczenia, ale dla 100? Roznicy.

To jak w Pythonie: `"".join(lista)` zamiast petli z `+=`.

---

## Krok 5: CraftingTable -- stol do craftowania

Czas polaczyc wszystko! CraftingTable sprawdza, czy masz skladniki w ekwipunku, zabiera je i daje wynik.

Stworz plik `crafting/CraftingTable.java`:

```java
package pl.codingtree.javanluzie.crafting;

import pl.codingtree.javanluzie.inventory.Inventory;
import pl.codingtree.javanluzie.item.Item;

public class CraftingTable {

    public static boolean craft(Recipe recipe, Inventory<Item> inventory) {
        System.out.println("\n--- Craftowanie: " + recipe.getName() + " ---");

        // Check ingredients
        for (Item ingredient : recipe.getIngredients()) {
            if (!inventory.contains(ingredient)) {
                System.out.println("Brakuje: " + ingredient.getName() + "!");
                return false;
            }
        }

        // Remove ingredients
        for (Item ingredient : recipe.getIngredients()) {
            inventory.remove(ingredient);
        }

        // Add result
        inventory.add(recipe.getResult());
        System.out.println("Stworzono: " + recipe.getResult() + "!");
        return true;
    }
}
```

### Jak to dziala?

1. **Sprawdz skladniki** -- petla sprawdza, czy kazdy skladnik jest w ekwipunku. Jesli brakuje chocby jednego -- craftowanie nie udaje sie.
2. **Usun skladniki** -- druga petla zabiera skladniki z ekwipunku.
3. **Dodaj wynik** -- gotowy przedmiot laduje do plecaka.

### `static` -- metoda bez obiektu

`craft()` jest `static` -- nie potrzebujesz obiektu CraftingTable, zeby jej uzyc:

```java
CraftingTable.craft(recipe, inventory);  // Wywolanie na KLASIE, nie obiekcie
```

To jak w sesji 1: `Arena.fight()` tez bylo statyczne. Statyczne metody to narzedzia -- nie potrzebuja stanu wewnetrznego.

### Dlaczego `Inventory<Item>` a nie `Inventory<Weapon>`?

Bo plecak bohatera trzyma **rozne** przedmioty -- bronie, materialy, zbroje. Gdybysmy przyjmowali `Inventory<Weapon>`, nie moglibysmy craft-owac z materialow!

---

## Krok 6: equals() i hashCode() -- tajemnica identycznosci

Pamietasz `equals()` i `hashCode()` w klasie Item? To kluczowy mechanizm, bez ktorego craftowanie **nie zadziala**.

### Problem

W Javie `==` porownuje **referencje** (adresy w pamieci), nie wartosci:

```java
Item drewno1 = new Item("Drewno", 0.5, Rarity.COMMON);
Item drewno2 = new Item("Drewno", 0.5, Rarity.COMMON);

System.out.println(drewno1 == drewno2);      // false! Rozne obiekty w pamieci
System.out.println(drewno1.equals(drewno2));  // true! Te same wartosci
```

Bez `equals()` metoda `inventory.contains(drewno)` NIGDY nie znalazlaby drewna, bo porownywala by adresy, a nie wartosci.

### Python comparison

W Pythonie `==` domyslnie tez porownuje referencje (jak `is`), ale zwykle nadpisujesz `__eq__`:

```python
def __eq__(self, other):
    return self.name == other.name and self.rarity == other.rarity
```

W Javie to samo, ale musisz tez nadpisac `hashCode()` -- to **kontrakt**. Jesli dwa obiekty sa `equals()`, MUSZA miec ten sam `hashCode()`. Inaczej kolekcje jak `HashMap` i `HashSet` sie pogubija.

### Regula: Nadpisujesz equals? Nadpisz hashCode!

Zawsze razem. Nigdy osobno. To zelazna zasada Javy.

---

## Krok 7: Laczymy wszystko -- Main.java

Zaktualizuj `Main.java`:

```java
package pl.codingtree.javanluzie;

import pl.codingtree.javanluzie.crafting.CraftingTable;
import pl.codingtree.javanluzie.crafting.Recipe;
import pl.codingtree.javanluzie.entity.Dragon;
import pl.codingtree.javanluzie.entity.Player;
import pl.codingtree.javanluzie.inventory.Inventory;
import pl.codingtree.javanluzie.item.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Java na Luzie - Sesja 2 ===\n");

        // Create player and inventory
        Player hero = new Player("Rycerz", 100, 15);
        Inventory<Item> plecak = new Inventory<>("Plecak", 10);

        // Add some items
        Item drewno = new Item("Drewno", 0.5, Rarity.COMMON);
        Item zelazko = new Item("Sztabka Zelaza", 1.0, Rarity.UNCOMMON);
        Weapon staryMiecz = new Weapon("Stary Miecz", 2.0, Rarity.COMMON, 10);

        plecak.add(drewno);
        plecak.add(zelazko);
        plecak.add(staryMiecz);
        plecak.show();

        // Fight dragon for loot
        Dragon dragon = new Dragon("Smaug", 80, 10);
        Arena.fight(hero, dragon);

        // Add dragon loot
        Item smoczeZloto = new Item("Smocze Zloto", 3.0, Rarity.LEGENDARY);
        plecak.add(smoczeZloto);

        // Craft a legendary sword
        Recipe epicMiecz = new Recipe("Smoczomiecz",
            List.of(staryMiecz, smoczeZloto, zelazko),
            new Weapon("Smoczomiecz", 3.5, Rarity.LEGENDARY, 50));

        CraftingTable.craft(epicMiecz, plecak);
        plecak.show();
    }
}
```

### Co sie tutaj dzieje -- scenariusz gry:

1. **Tworzymy bohatera i plecak** -- plecak ma miejsce na 10 przedmiotow.
2. **Zbieramy przedmioty** -- drewno, zelazko, stary miecz.
3. **Walczymy ze smokiem** -- uzywamy areny z sesji 1!
4. **Zdobywamy smocze zloto** -- legendarny lup po pokonaniu Smauga.
5. **Craftujemy Smoczomiecz** -- ze starego miecza, smoczego zlota i zelazka powstaje legendarna bron!

### Uruchom program!

```bash
mvn compile exec:java -Dexec.mainClass="pl.codingtree.javanluzie.Main"
```

Powinienes zobaczyc: zbieranie przedmiotow, walke ze smokiem, craftowanie legendarnego miecza i finalny stan plecaka!

---

## Podsumowanie -- czego sie dzis nauczyles

| Koncept | Co to robi | Python equivalent |
|---|---|---|
| **Enum** | Zamkniety zbior wartosci | `Enum` z modulu `enum` |
| **`final`** | Pole nie do zmiany po ustawieniu | Brak (konwencja `UPPERCASE`) |
| **Generyki `<T>`** | Typ jako parametr klasy | `list[int]` (type hints) |
| **`T extends Item`** | Ograniczenie generyka | Brak prawdziwego odpowiednika |
| **`ArrayList`** | Dynamiczna tablica | `list` |
| **`List` (interfejs)** | Kontrakt -- co obiekt umie | Brak (duck typing) |
| **Kompozycja** | "Ma" zamiast "jest" | Tak samo |
| **`equals()`** | Porownanie wartosci | `__eq__` |
| **`hashCode()`** | Hash do kolekcji | `__hash__` |
| **`StringBuilder`** | Efektywne laczenie stringow | `"".join()` |
| **`List.copyOf()`** | Niemutowalna kopia listy | `list(original)` + `tuple()` |

### Hierarchia naszych klas:

```
        Item
       /    \
   Weapon   Armor

   Inventory<T extends Item>    -- generyczna kolekcja

   Recipe  --zawiera--> List<Item>     -- kompozycja
   CraftingTable  --uzywa--> Recipe + Inventory  -- kompozycja
```

---

## BOSS FIGHT CHALLENGE: StackableItem

Czas na wyzwanie! W wielu grach przedmioty sie **stackuja** -- mozesz miec "Drewno x64" zamiast 64 osobnych Drewien w ekwipunku.

### Wymagania:

1. Stworz klase `StackableItem` ktora dziedziczy po `Item`.
2. Dodaj pole `int quantity` (ilosc) i `int maxStack` (max w stosie, np. 64).
3. Metoda `addToStack(int amount)` -- dodaje do stosu, ale nie wiecej niz maxStack. Zwraca ile sie NIE zmiescilo.
4. Metoda `removeFromStack(int amount)` -- usuwa ze stosu. Zwraca `true` jesli udalo sie usunac (jest wystarczajaco duzo).
5. Nadpisz `toString()` -- wyswietl nazwe i ilosc, np. `"Drewno x32 [Zwykly, 0.5kg]"`.
6. Nadpisz `equals()` -- dwa StackableItem sa rowne, jesli maja ta sama nazwe i rzadkosc (ilosc NIE ma znaczenia -- to wciaz ten sam typ przedmiotu).

### Podpowiedzi:

- `super.equals(o)` wywouje equals z Item -- nie musisz powtarzac logiki!
- Zastanow sie, czy `hashCode()` tez trzeba nadpisac. (Podpowiedz: jesli equals nie zmienilo logiki porownania... to hashCode z Item wystarczy!)

### Bonus:

Zmodyfikuj `Inventory` tak, zeby automatycznie stackowalo przedmioty. Gdy dodajesz drewno, a w ekwipunku juz jest drewno -- zwieksz stos zamiast zajmowac nowy slot!

Powodzenia, craftowiczu! W nastepnej sesji zbudujemy silnik gry z petla glowna, inputem gracza i prawdziwym gameplayem!
