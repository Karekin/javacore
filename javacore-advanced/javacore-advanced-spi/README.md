### 问题 1: 重载方法的区别与结果一致性
这两个重载方法有什么区别？结果一样吗？类加载器扮演的角色是什么？过滤吗？

#### 方法定义

**方法 1**（带类加载器参数）：
```java
public static <S> ServiceLoader<S> load(Class<S> service, ClassLoader loader) {
    return new ServiceLoader<>(service, loader);
}
```

**方法 2**（使用当前线程上下文类加载器）：
```java
public static <S> ServiceLoader<S> load(Class<S> service) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return ServiceLoader.load(service, cl);
}
```

#### 区别

- **参数区别**：
    - 方法 1：明确指定类加载器 `loader`，完全由调用方控制加载逻辑。
    - 方法 2：默认使用当前线程的上下文类加载器 `Thread.currentThread().getContextClassLoader()`。

- **调用链**：
    - 方法 2 是对方法 1 的封装，其等价调用为：
      ```java
      ServiceLoader.load(service, Thread.currentThread().getContextClassLoader());
      ```

#### 结果是否一致

- **结果一致性**：
    - 如果手动指定的 `loader` 等于当前线程的上下文类加载器（`Thread.currentThread().getContextClassLoader()`），则两种方法的结果是一样的。
    - 如果两者不相等，则结果取决于类加载器的加载范围。

- **差异场景**：
    - 当前线程的上下文类加载器无法加载目标类（例如，插件机制中插件实现类不在上下文类加载器的范围内）。
    - 需要明确指定某个特定类加载器（如插件专用的类加载器）来实现类加载隔离。

---

### 问题 2: 类加载器的角色与 SPI 机制中的作用
为什么下面的代码使用SPI机制时，调用了参数含类加载器的方法

类加载器在 SPI 机制中的主要作用包括：

1. **控制类的加载范围**：
    - 类加载器通过访问其对应的类路径或模块路径，定位并加载 `META-INF/services/<service-name>` 文件及服务实现类。
    - 不同类加载器拥有各自独立的加载范围，确保不同插件之间的类加载隔离。

2. **实现类过滤**：
    - 类加载器决定哪些实现类可被加载。如果某些类在当前类加载器不可见，它们不会出现在 `ServiceLoader` 的结果中。

3. **隔离插件环境**：
    - 在插件机制中，通常会为每个插件创建独立的类加载器，这样可以避免不同插件之间的类冲突。

---

### 问题 3: 为什么调用带类加载器参数的 SPI 方法？

代码分析：
```java
public <P> Iterator<P> load(Class<P> service) {
    try (TemporaryClassLoaderContext ignored =
                 TemporaryClassLoaderContext.of(pluginClassLoader)) {
        return new ContextClassLoaderSettingIterator<>(
                ServiceLoader.load(service, pluginClassLoader).iterator(), pluginClassLoader);
    }
}
```

#### 原因分析

1. **上下文类加载器的限制**：
    - 默认情况下，`ServiceLoader.load(service)` 使用的是当前线程的上下文类加载器。
    - 如果插件实现类只存在于 `pluginClassLoader` 的加载范围内，而默认上下文类加载器无法访问这些实现类，则会导致服务加载失败。

2. **明确插件加载器的作用**：
    - 手动指定 `pluginClassLoader` 确保 `ServiceLoader` 使用插件的类加载器，而不是默认的上下文类加载器。
    - 这可以避免因为类加载器隔离机制导致的加载问题。

3. **类加载隔离的实现**：
    - 插件机制中，为了保证插件之间互不干扰，通常会为每个插件分配独立的类加载器。
    - `pluginClassLoader` 被用于隔离插件的依赖和实现，确保每个插件运行在自己的类加载上下文中。

4. **临时切换类加载上下文**：
    - 使用 `TemporaryClassLoaderContext` 确保当前操作完全在 `pluginClassLoader` 的上下文中执行，进一步避免误用默认上下文类加载器的情况。

5. **SPI 的正确加载环境**：
    - `ServiceLoader.load(service, pluginClassLoader)` 明确告诉 SPI 使用哪个类加载器来定位和加载服务实现。
    - 这是插件机制中最佳实践，避免由于类加载器选择不当导致的服务发现失败或类冲突问题。

---

### 总结

- **方法区别**：
    - 带类加载器的方法适用于需要显式控制加载器的场景。
    - 无类加载器参数的方法使用上下文类加载器，适合默认加载器能够满足需求的情况。

- **类加载器的作用**：
    - 在 SPI 机制中，类加载器决定了实现类的可见性，类似于过滤器。
    - 插件机制中，通过独立类加载器实现类加载隔离，确保插件之间互不干扰。

- **为何使用带类加载器的 SPI 方法**：
    - 插件机制要求显式指定插件类加载器以实现隔离。
    - 避免默认的上下文类加载器无法加载插件实现类的问题。
    - 确保服务发现过程在正确的类加载上下文中执行，避免潜在的类加载冲突。