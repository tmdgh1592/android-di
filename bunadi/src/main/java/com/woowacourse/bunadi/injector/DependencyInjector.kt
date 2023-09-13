package com.woowacourse.bunadi.injector

import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.annotation.Singleton
import com.woowacourse.bunadi.module.Module
import com.woowacourse.bunadi.util.core.Cache
import com.woowacourse.bunadi.util.createInstance
import com.woowacourse.bunadi.util.parseFromQualifier
import com.woowacourse.bunadi.util.validateHasPrimaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {
    private val cache = Cache()

    fun <T : Any> inject(clazz: KClass<T>): T {
        val dependencyKey = DependencyKey.createDependencyKey(clazz)
        val cached = cache[dependencyKey]
        if (cached != null) return cached as T

        val primaryConstructor = clazz.validateHasPrimaryConstructor()
        val dependency = primaryConstructor.createInstance()
        injectMemberProperties(clazz, dependency)

        if (clazz.hasAnnotation<Singleton>()) {
            caching(dependencyKey, dependency)
        }
        return dependency
    }

    private fun <T : Any> injectMemberProperties(clazz: KClass<T>, instance: T) {
        clazz.memberProperties.forEach { property ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            if (property !is KMutableProperty<*>) return@forEach
            property.isAccessible = true

            val dependencyKey = DependencyKey.createDependencyKey(property)
            val realType = property.parseFromQualifier()
            val propertyInstance = inject(realType ?: property.returnType.jvmErasure)

            if (clazz.hasAnnotation<Singleton>()) {
                caching(dependencyKey, propertyInstance)
            }
            property.setter.call(instance, propertyInstance)
        }
    }

    fun module(module: Module) {
        val providers = module::class.declaredMemberFunctions
        providers.forEach { provider -> cache.caching(module, provider) }
    }

    fun caching(dependencyKey: DependencyKey, dependency: Any? = null) {
        cache.caching(dependencyKey, dependency)
    }

    fun clear() {
        cache.clear()
    }
}
