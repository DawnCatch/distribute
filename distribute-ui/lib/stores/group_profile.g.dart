// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'group_profile.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

String _$groupProfileStateHash() => r'f9252153b44fd2fc47467b4324d9a9e8dc9a08ce';

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

abstract class _$GroupProfileState
    extends BuildlessAutoDisposeAsyncNotifier<GroupProfile?> {
  late final num id;

  FutureOr<GroupProfile?> build(
    num id,
  );
}

/// See also [GroupProfileState].
@ProviderFor(GroupProfileState)
const groupProfileStateProvider = GroupProfileStateFamily();

/// See also [GroupProfileState].
class GroupProfileStateFamily extends Family<AsyncValue<GroupProfile?>> {
  /// See also [GroupProfileState].
  const GroupProfileStateFamily();

  /// See also [GroupProfileState].
  GroupProfileStateProvider call(
    num id,
  ) {
    return GroupProfileStateProvider(
      id,
    );
  }

  @override
  GroupProfileStateProvider getProviderOverride(
    covariant GroupProfileStateProvider provider,
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
  String? get name => r'groupProfileStateProvider';
}

/// See also [GroupProfileState].
class GroupProfileStateProvider extends AutoDisposeAsyncNotifierProviderImpl<
    GroupProfileState, GroupProfile?> {
  /// See also [GroupProfileState].
  GroupProfileStateProvider(
    num id,
  ) : this._internal(
          () => GroupProfileState()..id = id,
          from: groupProfileStateProvider,
          name: r'groupProfileStateProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$groupProfileStateHash,
          dependencies: GroupProfileStateFamily._dependencies,
          allTransitiveDependencies:
              GroupProfileStateFamily._allTransitiveDependencies,
          id: id,
        );

  GroupProfileStateProvider._internal(
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
  FutureOr<GroupProfile?> runNotifierBuild(
    covariant GroupProfileState notifier,
  ) {
    return notifier.build(
      id,
    );
  }

  @override
  Override overrideWith(GroupProfileState Function() create) {
    return ProviderOverride(
      origin: this,
      override: GroupProfileStateProvider._internal(
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
  AutoDisposeAsyncNotifierProviderElement<GroupProfileState, GroupProfile?>
      createElement() {
    return _GroupProfileStateProviderElement(this);
  }

  @override
  bool operator ==(Object other) {
    return other is GroupProfileStateProvider && other.id == id;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, id.hashCode);

    return _SystemHash.finish(hash);
  }
}

mixin GroupProfileStateRef
    on AutoDisposeAsyncNotifierProviderRef<GroupProfile?> {
  /// The parameter `id` of this provider.
  num get id;
}

class _GroupProfileStateProviderElement
    extends AutoDisposeAsyncNotifierProviderElement<GroupProfileState,
        GroupProfile?> with GroupProfileStateRef {
  _GroupProfileStateProviderElement(super.provider);

  @override
  num get id => (origin as GroupProfileStateProvider).id;
}
// ignore_for_file: type=lint
// ignore_for_file: subtype_of_sealed_class, invalid_use_of_internal_member, invalid_use_of_visible_for_testing_member
