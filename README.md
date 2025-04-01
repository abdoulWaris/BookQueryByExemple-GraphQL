# 📚 GraphQL Book API with Spring Boot & H2 Database

Ce projet est une API GraphQL développée avec **Spring Boot**, permettant de gérer des livres 📖.  
Elle utilise **H2 Database** en mémoire pour stocker les données et inclut des **tests GraphQL** avec `HttpGraphQlTester`.

---

## 🚀 Fonctionnalités

✅ **Créer, lire et rechercher des livres via GraphQL**  
✅ **Base de données H2 en mémoire** pour un démarrage rapide  
✅ **Tests automatisés avec JUnit & HttpGraphQlTester**  
✅ **Spring Data JPA pour interagir avec la base de données**  
✅ **Explorateur GraphiQL intégré (`http://localhost:8080/graphiql`)**  

---


## 🛠️ Installation & Exécution

### 📌 1. **Cloner le projet**
```sh
git clone https://github.com/ton-repo/GraphQL-Book-API.git
cd GraphQL-Book-API

### 📌 2. **Lancer le projet**
```sh
./mvnw clean install
ou
lance le directement sur ton IDE
```
Ensuite, ouvrez GraphiQL pour tester l'API :
`http://localhost:8080/graphiql`

## 📡 Requêtes GraphQL disponibles

### 🔍 **Rechercher tous les livres**
```sh
query {
  books {
    id
    title
    author
    publishedYear
  }
}
```

### 🔍 **Rechercher un livre par ID**
```sh
query {
  book(id: "4") {
    id
    title
    author
    publishedYear
  }
}
```
### 🔍 **Rechercher des livres par auteur et année de publication**
```sh
query {
  books(books: {
    author: "George Orwell"
    publishedYear: 1949
  }) {
    id
    title
    author
    publishedYear
  }
}
```

## 🏗️ Modifier le schéma GraphQL
Le fichier schema.graphqls définit la structure de l'API GraphQL.
Il est situé dans :

📂 `src/main/resources/graphql/schema.graphqls`
### Exemple de schéma actuel :
```sh
type Book {
  id: ID!
  title: String!
  author: String!
  publishedYear: Int!
}

type Query {
  books(books: BookInput): [Book]
  book(id: ID!): Book
}

input BookInput {
  title: String
  author: String
  publishedYear: Int
}

type Mutation {
  addBook(title: String!, author: String!, publishedYear: Int!): Book
}
```
