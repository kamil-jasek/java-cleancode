# WORKSHOP GOALS

1. Get to know what is good code design.
2. Effective work with refactoring tools.
3. Get to know how design patterns improves code quality and flexibility.

## Supporting materials:
1. [Clean code book](https://thixalongmy.haugiang.gov.vn/media/1175/clean_code.pdf)
2. [Refactoring book ](https://dl.ebooksworld.ir/motoman/Refactoring.Improving.the.Design.of.Existing.Code.2nd.edition.www.EBooksWorld.ir.pdf)
3. [Refactoring Guru](http://refactoring.guru)

## I. CLEAN CODE

### Why we need clean code?
1. We are mostly reading code, so it must be very easy to understand.
2. We are implementing new features, so it must be flexible and easily extensible.
3. We don't want to repeat some code.
4. We are testing functionalities, so it must be testable.
5. WE CARE ABOUT OUR CODE.

<br/>
<img src="https://redhat-hackathon.github.io/assets/img/fernando_technical_debt_guru_level_unlocked_badcode.jpg" width="500" alt="Code Quality">

### What is clean code?
1. It is elegant and efficient. 
2. It is flexible and expressive.
3. It is simple, and we can read it like a well-written prose.
4. It can be enhanced by a developer other than its original author.
5. It has unit and acceptance tests.
6. It provides one way rather than many ways for doing one thing.
7. It has minimal dependencies, which are explicitly defined.
8. It provides a clear and minimal API.
9. It always looks like it was written by someone who cares.
10. You can call it beautiful code when the code also makes it look like the language was made for the problem.

### Clean code toolbox:
1. Meaningful names.
2. Methods are short and do a single thing.
3. Use an argument object if a method needs many parameters.
4. Limit mutations and side effects.
5. Do something or return something - but not both.
6. SOLID, DRY, YAGNI, Prawo Demeter.
7. Don't add stupid comments.
8. Use comments only if they are: informational, explaining intentions, TODO, API.
9. Always format code with your code style.
10. Use carefully complex switch or if statements - maybe you need polymorphism.
11. Inheritance must be designed.
12. Prefer composition to inheritance.
13. Write loosely coupled code, and use design patterns.
14. EASILY UNDERSTANDABLE CODE ARCHITECTURE!!

## II. REFACTORING

### What is refactoring?
A series of small steps, each of which changes the internal structure of the program without changing the external behavior of the application.

### For what purpose do we use it?
1. The main purpose of refactoring is to fight technical debt. It transforms a mess into clean code and simple design. 
2. It is to lead to cheaper changes and extensions in the future.

### When to refactor code?
1. When creating new features:
   1. Preparing the code for new changes.
   2. Better understanding of code - low readability of code requires us to make changes.
2. During bugfixing - may indicate the need for refactoring.
3. During code review - code review indicates that the code can be improved.
4. When writing tests - current code may be difficult to test.
5. When you are working with source code and see violations of accepted Clean Code rules.

### Boy scout rule:
```text
Leave your code better than you found it.
```

### When does refactoring hurt?

#### I. Then when you don't have tests :(

What can you do about it?
1. Create tests first
2. Perform refactoring
3. It is possible that the current code is untestable - then refactoring is done along with writing tests.

#### II. Then when there is very low-quality code :(
What can you do about it?
1. It's faster to rewrite the code supporting the logic of the current code
2. May require you to recreate the functionality requirements that should be included in the tests

#### III. When the deadline is near :(
What can you do about it?
1. Postpone refactoring to another date and deliver the functionality
2. Postponed refactoring is called technology debt

### Refactoring rules:
Always support yourself with IDE in the process of refactoring your code:
1. when you rename class/method/variable
2. when you perform method extract/inline
3. when you move classes/methods
4. etc...

Refactor according to the accepted rules of Clean Code.
Your code must be better than the existing code in terms of:
1. Readability
2. Flexibility
3. Expressivity
4. etc...

Remember about continuous testing in each phase of refactoring.

### Refactoring toolbox:
1. Renaming fields, variables, constants, methods and classes
2. Change magic numbers/strings to constants
3. Extract methods - slimming down methods
4. Inline method - getting rid of methods that are too simple
5. Reducing method arguments using Argument Object
6. Grouping of similar and dependent methods
7. Encapsulation - protect access to object state
8. Seeking responsibility and elevating methods to separate classes
9. Swapping switch/if for polymorphism
10. Swapping error codes for exceptions
11. Loosening bindings and using design patterns
12. Preferring composition over inheritance
13. Organizing code in packages/modules - separating domain from presentation
14. And much more [here](https://www.refactoring.com/catalog/)

## III. TASKS

**Business Problem**

It's possible to make an order for any of the customers with items that have prices in different currencies. Every order has a base currency and all items must be exchanged for this currency. Every item has a product id, price (in any currency), quantity, and weight.

Order must have delivery cost calculated in base order currency. Delivery cost depends on the total order price and weight:
1. min price 400 and weight < 2kg then the delivery cost is 0.00 USD
2. min price 200 and weight < 1kg then the delivery cost is 15.00 USD
3. min price 100 and weight < 1kg then the delivery cost is 12.00 USD

For now, there is the next discount policy:
1. if there is a discount coupon and is active then the total order items price can be reduced by a percentage assigned to the coupon. After that discount coupon must be deactivated - it's one use only.
2. there are some days with free delivery, so then the delivery price can be added to the discount price.
3. in the future there is a plan to have a much more sophisticated discount policy.

Order must have calculated:
1. total price in base currency
2. delivery cost in base currency
3. discount in base currency
4. discounted total price plus delivery cost.
5. items with original price and exchanged to base currency
6. time when the order was made
7. assigned customer

When an order is made then it's confirmed, and it's waiting for further processing. Department responsible for sending products to customers must be notified via email about new order. In the future there is a plan to introduce more notification channels.

### 1. Code Review

1. Go to pull request in GitHub and perform code review: [PR Link](https://github.com/kamil-jasek/java-cleancode/pull/2/files)
2. Focus on OrderService class where is the main logic implemented.

### 2. Safe refactoring

1. Try to refactor as much as possible OrderService class in a safe way, mostly by using automatic refactoring tools. Avoid manual modifications.
2. If needed you can add more test cases to OrderServiceTest.

### 3. Extract classes and test them

1. Extract and write tests for order items price exchanger.
2. Extract and write tests for delivery cost calculator.
3. Extract and write tests for discount calculator.

### 4. Introduce port interfaces, avoid using mocks

1. Introduce interfaces (ports) for classes where is typical infrastructure code like CustomerService, DiscountService, CurrencyService etc.
2. Write test implementations for ports and then use those implementations in tests instead of mocks.  

### 5. Introduce domain objects

Entities are part of infrastructure. They are not fully validating business rules, and it's not easily possible to use them as model for our domain. Instead of entities we can implement domain objects that will fit more precisely to the business problem.

1. Implement domain objects and test them.
2. Move entities to the infrastructure layer.
3. Use domain objects instead of entities in services.
4. Map domain objects to entities in the infrastructure layer.

### 6. Design Patterns

#### 6.1. Composite

1. Try to implement discount calculator as composite pattern.
2. What technical issues you can find during implementation?

#### 6.2. Observer

1. Sending emails and other kinds of notifications must be extracted to separate components.
2. Try to think about what interfaces introduce to implement this pattern.
3. What technical issues you can find during implementation?

## IV. CLEAN CODE ARCHITECTURE
<br/>
<img src="https://mahedee.net/assets/images/posts/2021/clean.png" width="500" alt="Clean architecture">