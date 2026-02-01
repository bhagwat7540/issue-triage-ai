# AI-Powered Issue Triage & Prioritization (MVP)

A **hybrid AI system** that automatically analyzes production issues, finds similar historical incidents, and recommends **priority, ownership, and next action** using LLMs, embeddings (RAG), and deterministic decision logic.

---

## What this project does

- Accepts a raw issue description (text)
- Extracts structured features using an LLM
- Finds similar past issues using embeddings (RAG)
- Decides priority using historical patterns + rules
- Recommends team assignment and action
- Produces explainable, deterministic output

---

## High-Level Architecture

Issue text  
→ LLM (feature extraction)  
→ Embeddings (similarity search / RAG)  
→ PriorityAgent (urgency decision)  
→ RecommendationAgent (team + action)

---

## Core Components

### 1. IssueUnderstandingAgent (LLM)
- Converts unstructured text into structured features
- Example: category, component, urgency, affected users
- LLM is used **only for understanding**, not decision-making

---

### 2. EmbeddingService + SimilarIssueFinderService (RAG)
- Converts issues to vector embeddings
- Finds semantically similar historical issues
- Provides real context from past incidents
- Fully mockable (no paid API required for MVP)

---

### 3. PriorityAgent (Decision Engine)
- Uses historical priorities + business rules
- Applies hard safety overrides (e.g. outages)
- Produces confidence score
- Deterministic, explainable, testable
- **No LLM used here by design**

---

### 4. RecommendationAgent (Routing Engine)
- Assigns team based on historical ownership
- Chooses action based on priority
- Generates human-readable explanation
- Fully rule-based and deterministic

---

## Why hybrid AI (LLM + RAG + Rules)

- LLMs handle language understanding
- RAG provides real historical context
- Rules ensure safe and deterministic decisions
- System is explainable and production-ready
- Avoids letting LLMs make critical decisions

---

## Mock vs Real Services

| Component | MVP Mode | Production Mode |
|----------|----------|-----------------|
| LLM | MockLLMClient | OpenAI / Azure |
| Embeddings | MockEmbeddingService | Local model / API |
| Data Store | JSON | DB / Vector DB |

---

## How to Run

```bash
git clone <repo>
cd issue-triage-ai
mvn clean install
java Main
```

## 📈 Future Improvements

- Feedback loop from resolution outcomes
- Agent memory for learning over time
- Vector DB (FAISS / pgvector)
- Jira / PagerDuty / Slack integration
- SLA-aware prioritization
- Batch embedding & caching

## 🧑‍💻 Author

Built as a portfolio project to demonstrate **real-world AI system design**, not just LLM usage.
