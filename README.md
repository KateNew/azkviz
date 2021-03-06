# Uživatelská dokumentace

## Úvodní obrazovka
Stisknutím tlačítka "Vyber soubor" se otevře okno pro výběr souboru. Formát souboru viz dále. Poté se stisknutí tlačítka "Hrát" zahájí hra.

## Hrací obrazovka
Nahoře se zobrazuje pruh, který označuje, jaký hráč právě hraje (první hráč -- modrá, druhý hráč -- červená). Vlevo je trojúhelník pro výběr otázek, vpravo se zobrazují otázky a odpovědi. Stisknutím dosud nezahraného políčka (bílá barva) se zobrazí vlevo příslušná otázka. 
Pokud jde o otázku s možnostmi, zobrazí se všechny možnosti a hráč odpoví kliknutím na příslušnou možnost. Pokud je odpověď správná, políčko se obarví barvou hráče, který odpovídal a může hrát druhý hráč. 
Pokud je odpověď špatná, zobrazí se otázka "Chce odpovídat druhý hráč?" a tlačítka "Ano" a "Ne". Po kliknutí na "Ano" má druhý hráč možnost odpovědět. Po kliknutí na "Ne" se bere otázka jako špatně zopovězená a obarví se na šedo. 
Pokud druhý hráč odpoví správně, obarví se políčko jeho barvou a může odpovídat původní hráč. Pokud špatně, odpověd je rovnou špatně zodpovězená a obarví se na šedo. Poté opět odpovídá původní hráč.
Pokud jde o otázku s otevřenou odpovědí, spolu s otázku se objeví tlačítko "Odpověď". Po kliknutí na něj se objeví správná odpověď a tlačítka "Správně" a "Špatně". "Správně" označí tlačítko barvou hráče, "Špatně" na šedo jako špatně zodpovězenou otázku.

## Konec hry
Hra může skončit ze dvou důvodů. 
1. Nějaký hráč propojil všechny tři strany trojúhelníku. Pak hru vyhrál.
2. Už nezbývá žádné volné políčko. Vítězí hráč s větším počtem správně zodpovězených políček. Pokud mají oba stejně, jde o remízu.

## Formát otázek
### ABC otázky (otázky s možnostmi)
Na prvním řádku je číslo otázky (čísluje se od nuly) a počet možností N. Na dalším otázka, na dalším správná odpověď a následuje N řádku s možnostmi (včetně správné odpovědi).
Příklad:
````
0 3
Kolik možností má tato otázka?
tři
jednu 
dvě
tři
````

### OPEN otázky (otázky s otevřenou odpovědí)
Na první řádku je číslo otázky (čísluje se od nuly). Následují řádky s otázkou a správnou odpovědí.
Příklad:
````
1
Kolik slov má tato otázka?
čtyři
````
