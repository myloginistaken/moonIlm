EESTI KEELES

Luua Androidi rakendus, mis välisest RSS voost saadud andmete põhjal kuvaks hetkeilma. Prooviülesande eesmärgiks on näidata kasutajaliidese loomise, võrgühendusega ümberkäimise ja andmete parsimise oskust Android platvormil.
Aluseks võtta Ilmateenistuse RSS voog:
http://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php

PÕHIÜLESANNE

Laetakse sisse Ilmateenistuse RSS-i XML fail:
· Temperatuuride vahemik (min ... max)

· Kuvada temperatuuride vahemik ka teisendatuna sõnadesse (näiteks: kaksteist kraadi sooja või pluss kaksteist, viis kraadi külma või miinus viis)

· Ilma tekstiline kirjeldus

· Otsida RSS seest päeva <wind> elementide seest minimaalne ja maksimaalne väärtus ja kuvada see kasutajale tuule kiiruse vahemikuna (min ... max)

· Ilmainfo kuupäeva


Lisaks peab kasutajal võimaldama valida RSS-ist tulnud andmete põhjal ka teiste kuupäevade vahel mille kohta sama info kuvada.

MITTEKOHUSTUSLIKUD LISAÜLESANDED

1) Võimaldada kasutajal valitud kuupäeva kohta vaadata ka täpsemat ilma RSS-is kirjeldatud asukohtade jaoks (<place> elemendid). Kuvada tuleks:

· Koha nimi

· Temperatuuride vahemik (min. temperatuur tuleb öö infost, max. tuleb päeva infost)

· Ilma kirjeldus <phenomenom> välja kirjelduse järgi -- teisendada see eesti keelde

2) Lisada kasutajaliidesesse graafilisi elemente erineva ilmainfo edastamiseks (pilved, vihm jne)

3) Jätta meelde kasutaja viimati valitud asukoht ja rakenduse järgmisel avamisel kuvada ilmainfo eelmisel korral valitud asukoha jaoks.
