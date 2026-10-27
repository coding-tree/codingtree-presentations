# Coding Tree - Prezentacje

Interaktywne prezentacje dla szkoły programowania dla dzieci, opublikowane
pod adresem **https://prez.codingtree.pl/**.

## To jest kopia opublikowana

Nie edytuj prezentacji w tym repozytorium. Źródło jest w prywatnym
repozytorium `coding-tree/codingtree-materials` (`projects/presentations/`
oraz `learning-materials/advanced/java-na-luzie/`). Człowiek publikuje je
stamtąd skryptem:

```
scripts/publish-presentations.sh          # pokazuje, co się zmieni
scripts/publish-presentations.sh --push   # publikuje
```

Skrypt nadpisuje pliki `*.html` i katalog `tutorials/java-na-luzie/`.
`README.md`, `CNAME` i `LICENSE` zostają tutaj bez zmian.

## Prezentacje

Strona główna (`index.html`) dzieli je na zakładki według wieku i tematu.

| Zakładka | Plik | Tytuł |
|----------|------|-------|
| Malusi (6-9) | `wlacz-wylacz-pl.html` | Włącz / Wyłącz |
| Malusi (6-9) | `wzorce-pl.html` | Wzorce i Sekwencje |
| Malusi (6-9) | `pikselki-pl.html` | Pikselki |
| Malusi (6-9) | `algorytmy-pl.html` | Krok po Kroku |
| Malusi (6-9) | `czesci-komputera-pl.html` | Części Komputera |
| Starsi (9-12) | `binary-code-pl.html` | Kod Binarny - Język Komputerów! |
| Starsi (9-12) | `binary-arithmetic-pl.html` | Arytmetyka Binarna - Matematyka Komputerów! |
| Starsi (9-12) | `ascii-text-pl.html` | Jak Komputer Przechowuje Tekst |
| Starsi (9-12) | `logic-gates-pl.html` | Bramki Logiczne - Jak Komputer Podejmuje Decyzje! |
| Starsi (9-12) | `pixels-rgb-pl.html` | Od Bitów do Pikseli - Jak Komputer Rysuje Obrazki! |
| Scratch | `mini-szachy-bloki-pl.html` | Mini Szachy - Prezentacja Scratch (polski) |
| Scratch | `mini-chess-blocks-presentation.html` | Mini Chess - Scratch Blocks Presentation (English) |
| Scratch | `mini-chess-presentation.html` | Mini Chess - Scratch Project Presentation (English) |
| Java | `java-01-swiat-pl.html` | Stwórz Swój Świat - Java na Luzie |
| Java | `java-02-ekwipunek-pl.html` | Ekwipunek Bohatera - Java na Luzie |
| Java | `java-03-silnik-pl.html` | Silnik Gry - Java na Luzie |
| Java | `java-04-supermoc-pl.html` | Supermoc Javy - Java na Luzie |
| Liceum (15-19) | `jezyki-programowania-pl.html` | Języki Programowania — Mapa Świata |

`tutorials.html` prowadzi do tutoriali, na razie do kursu „Java na Luzie”
(`tutorials/java-na-luzie/`, sesje 1–4 i projekt).

## Użycie

Otwórz https://prez.codingtree.pl/ albo dowolny plik `.html` bezpośrednio w
przeglądarce. Prezentacje nie mają zależności do zainstalowania.

## Nawigacja

- Strzałki ← → lub przyciski na slajdzie
- Spacja = następny slajd
- „Języki Programowania — Mapa Świata” to mapa, nie slajdy: przycisk
  „💡 Ciekawostki” przy rodzinie języków otwiera okienko, a Escape je zamyka.

## Licencja

CC BY-NC 4.0, zobacz `LICENSE`.
