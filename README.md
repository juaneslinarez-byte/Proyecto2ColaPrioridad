# Sistema de Cola de Impresión con Prioridades

Este es un proyecto donde hice un simulador de cola de impresión. La idea es que los usuarios tienen diferentes prioridades (alta, media, baja) y los documentos se organizan usando un heap para que siempre se imprima el más urgente primero. También tiene una tabla hash para buscar documentos rápido.

**Hecho por:** Juan Linarez 

## Qué hace el programa

Básicamente simula una impresora compartida donde:
- Hay usuarios con distintas prioridades
- Puedes crear documentos y mandarlos a imprimir
- Los documentos prioritarios se adelantan en la cola
- Puedes ver la cola como lista o como árbol
- Todo tiene interfaz gráfica para que sea fácil de usar

## Cómo está hecho

Usé dos estructuras de datos principales que implementé desde cero (sin usar las de Java):

**Montículo Binario (Min-Heap)** - Para manejar la cola con prioridades. El documento con menor "etiqueta de tiempo" siempre sale primero.

**Tabla Hash** - Para buscar rápido los documentos de un usuario específico. Usa encadenamiento para manejar colisiones.

También usé GraphStream para mostrar el heap como un árbol visual, que es bastante útil para entender cómo funciona.

Necesitas Java 25 y las librerías GraphStream que ya están incluidas en la carpeta 

## Repo

https://github.com/juaneslinarez-byte/Proyecto2ColaPrioridad

