
## One Pager – Gebruik van Spring ApplicationEvents

### Inleiding

Om afhankelijkheden te verminderen en de architectuur beter uitbreidbaar te maken, zou ik **Spring ApplicationEvents** inzetten binnen elke microservice.

---

### Toepassing

Ik zou Spring ApplicationEvents gebruiken voor **interne asynchrone communicatie binnen één service**.

* **Bijvoorbeel in de Order Service**:
  Wanneer een bestelling wordt geplaatst, zou de `OrderPlacedEvent` automatisch meerdere listeners triggeren:

    * één listener die de status initialiseert en de order opslaat in de database;
    * één listener die een bericht naar RabbitMQ stuurt om andere services (Restaurant, Delivery) op de hoogte te brengen;
      Zo wordt de `OrderService` zelf niet overbelast met bijkomende verantwoordelijkheden.

Op deze manier wordt de domeinlogica opgesplitst in *producers* (die events publiceren) en *listeners* (die op een event reageren). 

---

### Voordelen

* **Lagere koppeling (loose coupling):** de services en componenten kennen elkaar niet rechtstreeks, enkel via events.
* **Betere testbaarheid:** listeners kunnen afzonderlijk getest worden, zonder dat de hele keten hoeft te draaien.
* **modulaire architectuur:** elk event vormt een duidelijke grens tussen domeinacties.

---

### Nadelen

* **Complexere flow:** events worden asynchroon verwerkt, waardoor de volgorde van acties minder voorspelbaar wordt en debugging moeilijker is.
* **Risico op spaghetti:** te veel losse listeners kunnen de controle over de flow verminderen als er geen duidelijke structuur is.
* **Foutafhandeling:** bij asynchrone listeners is het lastiger om fouten door te geven naar de oorspronkelijke afzender.

---

### Conclusie

Door **Spring ApplicationEvents** te gebruiken binnen elke microservice, kan er een meer modulaire, uitbreidbare en onderhoudbare architectuur gemaakt worden. Het zorgt voor een duidelijke scheiding van verantwoordelijkheden tussen domeinlogica, infrastructuur en externe integraties. 

---
