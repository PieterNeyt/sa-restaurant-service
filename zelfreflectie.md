# Zelfreflectie document - voorbeeld

## V1

### 🔗 Pre coaching

**Geschatte Progress (in procent): 25%**

---

#### Status
*Waar sta je globaal? Wat loopt goed en minder goed? Hoe verloopt de samenwerking? Wie heeft geholpen en wie kan de applicatie implementeren / delen? Passen kunnen er bekijken op het niveau team?*

De eerste week hebben we ons vooral bezig gehouden met het opzetten van alle 3 services, hun repositories en het domeinmodel.
Na de DDD-sessie en de persistency-sessie hebben we ons domein gerefactord

We hebben **US1-2-3-4-5-6-7-8-9-10-12-39** al afgewerkt (dit is bijna heel de restaurant service behalve US40 wat gaat over integratietests schrijven)

We gaan nu beginnen aan de orderservice. We moeten juist nog eens kijken hoe we hier juist aan gaan beginnen.



---

#### Stories
*Geef aan welke stories zijn opgepakt en afgerond.*

- [US1-2-3-4-5-6-7-8-9-10-12-39] → afgerond 


---

#### Quality
*Acties / refactoring die nog gepland staan om de kwaliteit van je project te verhogen. Maak hiervoor issues aan: issue summary, toelichting (issue nummer), toelichting.*

* we moeten  nog eens samenzitten om te zien of we de ddd-princiepes met de aggregate root wel goed hebben toegepast bij het bewerken/updaten of toevoegen van een entity
---

#### Vragen
*Eventuele vragen voor je coach*

- / Voorlopig niet

---

### Post coaching

#### Feedback
*Zijn de velden na gesprek? Wie zorgde voor verschil qua plaatsing t.o.v. het single responsibility principe en moeten het project overlopen om dit op orde te krijgen. Er zit business logica in de Controller die daar niet thuis hoort vs. prijsberekening. We moeten erover waken dat we dit scheiden, want we lopen een beetje achter.*

Stories toewijzen 
oppassen met getters
Naamgeving packages met kleine letters
Naamgeving methodes duidelijker maken bv Put en changes is teveel van het goedde + change in restaurantservice
 --> als publishing dish wordt gescheduled moet er worden nagekeken of er al 10 published dishes zijn