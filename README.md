# Wallet Microservice

## 🔧 How to Use the Service

Examples of requests to the service are created with Postman and can be found here:  
➡ **[Postman Collection](https://documenter.getpostman.com/view/2855941/2sA2rCTMAw)**

---

## 📄 Description

A monetary account holds the current balance for a wallet.  
The balance can be modified by registering transactions on the account:

- **Debit transactions** – remove funds
- **Credit transactions** – add funds

This is a REST API implementation that fulfills the requirements described below.

---

## ✅ Functional Requirements

- View **current balance** per wallet
- **Debit/Withdrawal**:
  - Only succeeds if there are sufficient funds (`balance - debit amount >= 0`)
- **Credit**:
  - Adds funds to a wallet
- **Transaction ID** must be unique for all transactions:
  - If not, the operation must fail
- **Transaction history** per wallet
- Custom exceptions for:
  - Duplicate transaction ID
  - Wallet ID not found in database
  - Insufficient balance for debit transactions

---

## 📌 Non-Functional Requirements

- Clean and modular **design**
- **Readable, maintainable code**
- **Testable** components with unit/integration tests
