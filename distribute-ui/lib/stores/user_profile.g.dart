// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user_profile.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$userProfileStateHash() => r'e2693d97f2567f6e5e4d60f55c1861df595251dd';

/// Copied from Dart SDK
class _SystemHash {
  _SystemHash._();

  static int combine(int hash, int value) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + value);
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x0007ffff & hash) << 10));
    return hash ^ (hash >> 6);
  }

  static int finish(int hash) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x03ffffff & hash) << 3));
    // ignore: parameter_assignments
    hash = hash ^ (hash >> 11);
    return 0x1fffffff & (hash + ((0x00003fff & hash) << 15));
  }
}

abstract class _$UserProfileState
    extends BuildlessAutoDisposeAsyncNotifier<UserProfile?> {
  late final num id;

  FutureOr<UserProfile?> build(
    num id,
  );
}

/// See also [UserProfileState].
@ProviderFor(UserProfileState)
const userProfileStateProvider = UserProfileStateFamily();

/// See also [UserProfileState].
class UserProfileStateFamily extends Family<AsyncValue<UserProfile?>> {
  /// See also [UserProfileState].
  const UserProfileStateFamily();

  /// See also [UserProfileState].
  UserProfileStateProvider call(
    num id,
  ) {
    return UserProfileStateProvider(
      id,
    );
  }

  @override
  UserProfileStateProvider getProviderOverride(
    covariant UserProfileStateProvider provider,
  ) {
    return call(
      provider.id,
    );
  }

  static const Iterable<ProviderOrFamily>? _dependencies = null;

  @override
  Iterable<ProviderOrFamily>? get dependencies => _dependencies;

  static const Iterable<ProviderOrFamily>? _allTransitiveDependencies = null;

  @override
  Iterable<ProviderOrFamily>? get allTransitiveDependencies =>
      _allTransitiveDependencies;

  @override
  String? get name => r'userProfileStateProvider';
}

/// See also [UserProfileState].
class UserProfileStateProvider extends AutoDisposeAsyncNotifierProviderImpl<
    UserProfileState, UserProfile?> {
  /// See also [UserProfileState].
  UserProfileStateProvider(
    num id,
  ) : this._internal(
          () => UserProfileState()..id = id,
          from: userProfileStateProvider,
          name: r'userProfileStateProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$userProfileStateHash,
          dependencies: UserProfileStateFamily._dependencies,
          allTransitiveDependencies:
              UserProfileStateFamily._allTransitiveDependencies,
          id: id,
        );

  UserProfileStateProvider._internal(
    super._createNotifier, {
    required super.name,
    required super.dependencies,
    required super.allTransitiveDependencies,
    required super.debugGetCreateSourceHash,
    required super.from,
    required this.id,
  }) : super.internal();

  final num id;

  @override
  FutureOr<UserProfile?> runNotifierBuild(
    covariant UserProfileState notifier,
  ) {
    return notifier.build(
      id,
    );
  }

  @override
  Override overrideWith(UserProfileState Function() create) {
    return ProviderOverride(
      origin: this,
      override: UserProfileStateProvider._internal(
        () => create()..id = id,
        from: from,
        name: null,
        dependencies: null,
        allTransitiveDependencies: null,
        debugGetCreateSourceHash: null,
        id: id,
      ),
    );
  }

  @override
  AutoDisposeAsyncNotifierProviderElement<UserProfileState, UserProfile?>
      createElement() {
    return _UserProfileStateProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is UserProfileStateProvider && other.id == id;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, id.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin UserProfileStateRef on AutoDisposeAsyncNotifierProviderRef<UserProfile?> {
  /// The parameter `id` of this provider.
  num get id;
}

class _UserProfileStateProviderElement
    extends AutoDisposeAsyncNotifierProviderElement<UserProfileState,
        UserProfile?> with UserProfileStateRef {
  _UserProfileStateProviderElement(super.provider);

  @override
  num get id => (origin as UserProfileStateProvider).id;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
