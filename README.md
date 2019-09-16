A technical test for Android development.

## Introduction:

First off, thanks for taking the time to apply to our organization. We really appreciate the interest, and we know your time is valuable. We hope that this test will give you a little insight into some of the technologies and systems we deal with on an everyday basis, in addition to helping us get a better idea of your strengths and how what you can bring to our team.

## The Assignment:

#### Your job is to create a basic Android health application that does the following core tasks. You may use the included HAPI library, or any other REST+JSON solution of your choice:

When you're ready to start, begin by cloning this repo. We've configured a skeleton project for you in the `/android` folder.

1. Download the 10 most recently updated patients from the test server.
  * Just as a note, patient objects are complicated...ignore all the fields you do not need when you pull the data from the server. Don't create a tonne of POJOs to cover every field when you download the patient object.
2. Display a list of these patients to the user, by name. 
3. Allow the user to view the given/family name, gender, and birthday of a specific patient if they want.
4. Allow the user to edit the given/family name, gender, and birthday of a specific patient if they want.
5. Update the modified patient onto the test server.
6. Upload the project to a public Git code repository so we can clone and look at what you've done.

## Your Toolbox:

There are a few pieces of knowledge you will need to effectively complete the assignment.

### 1. [A Brief Introduction to FHIR](fhir.md)

### 2. [The Test Server](server.md)

### 3. [HAPI Client documentation (Optional)](https://hapifhir.io/doc_rest_client.html)

If you choose to use the HAPI client, here's a snippet to get you started:

```java
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

...

// *** Use the DSTU 3 data model. The newer R4 model has limited Android support.
FhirContext context = FhirContext.forDstu3();

// *** Use the baseDstu3 server URL.
// The newer baseR4 URL used in the test server examples has limited Android support.
IGenericClient client = context.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");

// *** Take caution specifying search parameters, too many and the server times out.
Bundle bundle = client.search().forResource(Patient.class)
        .where(Patient.NAME.isMissing(false))
        .and(Patient.BIRTHDATE.isMissing(false))
        .returnBundle(Bundle.class)
        .execute();
```

### 4. [FHIR Patient documentation (Optional)](http://hl7.org/fhir/STU3/patient.html)

If you choose to make direct REST calls instead of using the HAPI library, you can start with:

`GET http://fhirtest.uhn.ca/baseDstu3/Patient?_format=json&name:missing=false&birthdate:missing=false`

## What are we looking for?

 1. Does the app accurately do what it's supposed to do?
 2. Code quality. Error handling. Code comments/documentation. We deal with some of peoples most sensitive data, their personal health information. The code we write needs to be readable, dependable, and well documented.
 3. Unit tests. How do you know that the code is doing what it's supposed to do? At minimum, I would expect you to test that you can get and set the patient fields correctly.
 4. Does it look good? It doesn't have to be a piece of art, but it would be good if the application followed the [google material design guidelines](https://material.io/guidelines/) to some degree. Medical apps don't work if people think they're ugly, unintuitive, and don't use them.
 5. What libraries are you using? We don't expect you to home roll your own REST framework, or JSON deserializer. How familiar are you with the most commonly used Android development tools available?
 6. We are primarily a Java shop, so please stick to Java in Android Studio.
 
Just keep in mind, that like most programming problems, there is rarely a single right or wrong answer. Do the work to the best of your abilities. If there is a section or part you are uncertain of, make an assumption and be sure to document it in the solution you provide. We're not here to try and trick you. If you cannot complete everything, submit what you have, and we can talk about the solution you've provided.

We wish you the best of luck, and thanks again for your interest!
