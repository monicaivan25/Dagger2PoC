# Dagger2 Proof of Concept
In OOP, classes **depend** on other classes all the time, because each class is supposed to have a small number of responsabilities, and relies on other classes to do their work(e.g RecyclerView depends on Adapter). 

## Dependency Injection
[Dependency Injection](https://www.freecodecamp.org/news/a-quick-intro-to-dependency-injection-what-it-is-and-when-to-use-it-7578c84fa88f/) refers to receiving dependencies from the outside. Classes shouldn't be responsible for creating their own dependencies, or searching for them, instead the dependencies should be instantiated somewhere else and then passed to the class that needs them.
Decoupling classes can be obtained by passing the necessary objects through the constructor or through getters/setters.
```java
//before
Car(){
  engine = new Engine();
  wheels = new Wheels();
}
```
```java
//after
Car(Engine engine, Wheels wheels){
  this.engine = engine;
  this.wheels = wheels;
}
```
## Dagger2 
Dependency injection can be obtained with or without any frameworks. So what is the use of Dagger2?

The main responsability of dependency injection frameworks like Dagger2 is to get rid of all the boilerplate code that comes with manual dependency injection.

```java
//manual dependency injection
Block block = new Block();
Cylinders cylinders = new Cylinders();
SparkPlugs sparkPlugs = new SparkPlugs();
Tires tires = new Tires();
Rims rims = new Rims();

Engine engine = new Engine(block, cylinders, sparkPlugs);
Wheels wheels = new Wheels(tires, rims);

Car car = new Car(engine, wheels);
```

```java
//dependency injection using Dagger2
CarComponent component = DaggerCarComponent.create();
Car car = component.getCar();
```

# Using Dagger2

## 1. Add Dagger gradle dependencies
```gradle
implementation 'com.google.dagger:dagger:2.x'
annotationProcessor 'com.google.dagger:dagger-compiler:2.x'
``` 

## 2. Creating the Component Interface
The component interface is the backbone of Dagger. This is where our Activity can get the instantiated object it needs.

For this, the component creates a dependency graph. It knows the Car needs an Engine and Wheels, and about the other dependencies further down the road. It also knows in what order it has to create them. The schema is a Directed Acyclic Graph, DAG, where the name Dagger comes from.
![Dependency graph](https://i.imgur.com/qZEmMQF.png)

The component, also called the Injector, **creates and stores** our objects, and then **provides** them to us.

The Component interface needs the `@Component` annotation.
```java
import dagger.Component; 

@Component
public interface CarComponent{
}
```

### 2.1. Provision Methods
In our Component interface we need to create a simple getter method. Annotation processing will turn the `@Component` anotation into usable code.
```java
@Component
public interface CarComponent{
  Car getCar();
}
```
To get an instance of a ```CarComponent```, we must first compile the code, which will have Dagger create the ```DaggerCarComponent```, which is the implemementation for our interface.

## 3. Injection

Compiling the code at this state will result in compile time errors, because we need to tell Dagger which constructors or fields he needs to inject.

### 3.1 Constructor Injection 
In order for Dagger to instantiate our Car object, we need to mark all the constructors it is supposed to use using the `@Inject` annotation.

***The `@Inject` annotation can only be used on a single constructor per class.***

```java
public class Car {
    private Engine engine;
    private Wheels wheels;

    @Inject
    public Car(Engine engine, Wheels wheels) {
        this.engine = engine;
        this.wheels = wheels;
    }

    public void drive(){
        Log.d(TAG, "driving");
    }
}
```
Here `@Inject` has been used on a constructor that uses 2 other types of classes, Engine and Wheels. We must also annotate their constructors with `@Inject`. 

```java
    @Inject
    public Engine() {}
```
```java
    @Inject
    public Wheels(){}
```
### 3.2 Field Injection

