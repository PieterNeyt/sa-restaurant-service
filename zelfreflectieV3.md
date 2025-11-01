
# Zelfreflectie Document – V3


**Geschatte progressie:** 100%

---

###  Status

**Alle issues zijn afgerond.**
Alle functionaliteiten die binnen de scope van het project vielen, zijn geïmplementeerd en getest. 

---

###  Stories

Alle user stories uit de backlog zijn opgepakt, toegewezen en afgerond.
De belangrijkste onderdelen zoals restaurantbeheer, bestellingafhandeling, deliverylogica en de koppeling via RabbitMQ zijn volledig uitgewerkt.


---

###  Quality

We hebben volgende zaken toegepast in verband met code-qualtiy:

* **Testing:**
  Zowel unit als integratietests zijn voorzien volgens de vereisten.
  De *delivery service* behaalt minstens 80% testdekking met unit tests, terwijl de *restaurant service* minstens 60% coverage haalt via integrationtests.

* **Architectuur & codekwaliteit:**
  De code volgt de principes van *loose coupling* en *single responsibility*, met duidelijke lagen (controller, service, repository).
  Binnen de microservices worden configuraties en businessregels gescheiden gehouden, en services communiceren enkel via hun publieke interfaces of via events.

* **Configuratie & logging:**
  Alle belangrijke parameters (zoals beslisperiodes, vergoedingen en databaseverbindingen) zijn configureerbaar gemaakt zodat er geen magic numbers zijn via de `application.properties`.

* **Security:**
  De beveiliging is  toegepast voor de *restaurant* en *delivery services*, met gebruik van de rollen *owner*, *driver* en *admin*.
  De nodige endpoints zijn afgeschermd volgens hun rol.

* **Kwaliteitsprincipes:**
  We hebben ook ons best gedaan om de DDD-principes zo goed mogelijk te volgen 

Daarnaast hebben we de feedback uit de sprint 1- en sprint 2-reviews grondig verwerkt. 

---
