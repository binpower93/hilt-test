# hilt-test

## Development Setup

**Android Studio**: 4.2 Canary 4

**Min SDK**: 21 (5.0)

**Target & Compile SDK**: 30

**Kotlin Version**: 1.3.72

## Api

**GET /posts.json**

Example:
```
    {
      "-MDG9j-TbGc-diDPIflK": {
        "content": "Test content",
        "id": "testid",
        "title": "Test Title"
      },
      "-MDGCMeN0gbxr0r4rrEN": {
        "content": "rtrt",
        "id": "40249254-830c-4daf-bf21-9b5ad682184f",
        "title": "rttr"
      },
      "-MDGFWPB149yqa2xry_n": {
        "content": "ghfghfghf",
        "id": "e14c9543-8f57-4dff-91bf-cea030400c18",
        "title": "hhgfhfg"
      }
    }
```

**POST /post.json**

Example Body:
```
    {
        "id": "test-id",
        "name": "Name",
        "content": "Content"
    }
```

Example Response:
```
    {
        "name": "-MDGCMeN0gbxr0r4rrEN"
    }
```
